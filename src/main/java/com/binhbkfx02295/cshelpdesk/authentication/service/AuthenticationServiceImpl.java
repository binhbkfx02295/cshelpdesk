package com.binhbkfx02295.cshelpdesk.authentication.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.authentication.dto.LoginRequestDTO;
import com.binhbkfx02295.cshelpdesk.authentication.dto.LoginResponseDTO;
import com.binhbkfx02295.cshelpdesk.authentication.dto.UserPrincipal;
import com.binhbkfx02295.cshelpdesk.authentication.util.ValidationHelper;
import com.binhbkfx02295.cshelpdesk.authentication.util.ValidationResult;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Status;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.StatusLogRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.websocket.event.EmployeeEvent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Locale locale = Locale.forLanguageTag("vi");
    private final EmployeeRepository employeeRepository;
    private final StatusLogRepository statusLogRepository;
    private final ValidationHelper validationHelper;
    private final MessageSource messageSource;
    private final MasterDataCache cache;
    private final EmployeeMapper employeeMapper;
    private final EntityManager entityManager;
    private final ApplicationEventPublisher publisher;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public APIResultSet<LoginResponseDTO> login(LoginRequestDTO request) {
        Authentication token = null;
        try {
        token = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException ex) {
            log.info("Username & password: " + request.getUsername() + " / " + request.getPassword());
            log.info("Sai ten dang nhap hoac mat khau");
            throw ex;
        }
        UserPrincipal userPrincipal = (UserPrincipal) token.getPrincipal();
        Employee employee = userPrincipal.getEmployee();
        SecurityContextHolder.getContext().setAuthentication(token);
        
        statusLogRepository.findFirstByEmployee_UsernameOrderByTimestampDesc(employee.getUsername()).ifPresentOrElse(
                statusLog -> {

                    if (statusLog.getStatus().getId() != 1) {
                        StatusLog newLog = new StatusLog();
                        Status status = cache.getStatus(1);
                        newLog.setStatus(status);
                        newLog.setEmployee(employee);
                        employee.getStatusLogs().add(newLog);
                    }
                },
                () -> {
                    StatusLog newLog = new StatusLog();
                    Status status = cache.getStatus(1);
                    newLog.setStatus(status);
                    newLog.setEmployee(employee);
                    employee.getStatusLogs().add(newLog);
                });
        employeeRepository.save(employee);
        entityManager.flush();
        entityManager.clear();
        cache.updateAllEmployees();

        publisher.publishEvent(new EmployeeEvent(EmployeeEvent.Action.UPDATED,
                employeeMapper.toDashboardDTO(cache.getEmployee(employee.getUsername()))));

        return APIResultSet.ok(messageSource.getMessage("auth.login.success", null, locale),
                LoginResponseDTO.builder().employeeDTO(employeeMapper.toDTO(cache.getEmployee(employee.getUsername())))
                        .build());
    }

    @Override
    @Transactional
    public APIResultSet<Void> logout(String username) {
        SecurityContextHolder.getContext().setAuthentication(null);
        Optional<Employee> employeeOpt = employeeRepository.findWithAllStatusLog(username);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            List<StatusLog> logs = employee.getStatusLogs();
            StatusLog statusLog = logs.get(logs.size() - 1);
            if (statusLog.getStatus().getId() != 3) {
                StatusLog newLog = new StatusLog();
                Status status = cache.getStatus(3);
                newLog.setStatus(status);
                newLog.setEmployee(employee);
                employee.getStatusLogs().add(newLog);
                employeeRepository.saveAndFlush(employee);
                entityManager.flush();
                entityManager.clear();
                cache.updateAllEmployees();

                publisher.publishEvent(new EmployeeEvent(EmployeeEvent.Action.UPDATED,
                        employeeMapper.toDashboardDTO(cache.getEmployee(employee.getUsername()))));
            }
        }
        return APIResultSet.ok(messageSource.getMessage("auth.logout.success", null, locale), null);
    }

    @Override
    public ValidationResult validate(LoginRequestDTO request, Locale locale) {
        return validationHelper.validateLoginInput(request.getUsername(), request.getPassword(), locale);
    }
}

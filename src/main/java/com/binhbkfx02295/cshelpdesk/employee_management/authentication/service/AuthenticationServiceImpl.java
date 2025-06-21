package com.binhbkfx02295.cshelpdesk.employee_management.authentication.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.service.EmployeeService;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.employee_management.authentication.dto.LoginRequestDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.authentication.dto.LoginResponseDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.authentication.util.ValidationHelper;
import com.binhbkfx02295.cshelpdesk.employee_management.authentication.util.ValidationResult;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Status;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.StatusLogRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PasswordValidator;
import com.binhbkfx02295.cshelpdesk.websocket.event.EmployeeEvent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final ValidationHelper validationHelper;
    private final MessageSource messageSource;
    private final MasterDataCache cache;
    private final EmployeeMapper employeeMapper;
    private final EntityManager entityManager;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public APIResultSet<LoginResponseDTO> login(LoginRequestDTO request) {
        LoginResponseDTO response;
        ValidationResult validation = validate(request, locale);
        if (validation.hasErrors()) {
            response = new LoginResponseDTO();
            response.setValidationResult(validation);
            return APIResultSet.badRequest(messageSource.getMessage("auth.invalid.credentials", null, locale), response);
        }
        Optional<Employee> employeeOpt = employeeRepository.findWithUserGroupAndPermissionsByUsername(request.getUsername());
        if (employeeOpt.isEmpty()) {
            return APIResultSet.badRequest(messageSource.getMessage("auth.invalid.credentials", null, locale));
        }
        Employee employee = employeeOpt.get();

        if (!employee.isActive()) {
            return APIResultSet.forbidden(messageSource.getMessage("auth.account.locked", null, locale));
        }
        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            int failCount = employee.getFailedLoginCount() + 1;
            employee.setFailedLoginCount(failCount);
            if (failCount >= 5) {
                employee.setActive(false);
            }
            employeeRepository.save(employee);
            return APIResultSet.unauthorized(messageSource.getMessage("auth.invalid.credentials.remaining", new Object[]{5 - failCount}, locale));

        }
        employee.setFailedLoginCount(0);

        statusLogRepository.findFirstByEmployee_UsernameOrderByTimestampDesc( employee.getUsername()).ifPresentOrElse(
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
        response = new LoginResponseDTO();
        EmployeeDTO employeeDTO = employeeMapper.toDTO(cache.getEmployee(employee.getUsername()));
        response.setEmployeeDTO(employeeDTO);
        publisher.publishEvent(new EmployeeEvent(EmployeeEvent.Action.UPDATED, employeeMapper.toDashboardDTO(cache.getEmployee(employee.getUsername()))));

        return APIResultSet.ok(messageSource.getMessage("auth.login.success", null, locale), response);
    }

    @Override
    @Transactional
    public APIResultSet<Void> logout(EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOpt = employeeRepository.findWithAllStatusLog(employeeDTO.getUsername());

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            List<StatusLog> logs = employee.getStatusLogs();
            StatusLog statusLog = logs.get(logs.size()-1);
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

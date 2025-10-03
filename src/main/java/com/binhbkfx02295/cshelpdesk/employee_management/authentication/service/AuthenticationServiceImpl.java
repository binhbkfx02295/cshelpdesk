package com.binhbkfx02295.cshelpdesk.employee_management.authentication.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.employee_management.authentication.dto.LoginRequest;
import com.binhbkfx02295.cshelpdesk.employee_management.authentication.dto.LoginResponse;
import com.binhbkfx02295.cshelpdesk.employee_management.authentication.util.ValidationHelper;
import com.binhbkfx02295.cshelpdesk.employee_management.authentication.util.ValidationResult;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Status;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.StatusLogRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.websocket.event.EmployeeEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountLockedException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final EmployeeRepository employeeRepository;
    private final StatusLogRepository statusLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final MasterDataCache cache;
    private final EmployeeMapper employeeMapper;
    private final ApplicationEventPublisher publisher;

    @Override
    public LoginResponse login(LoginRequest request) throws AccountLockedException {
        LoginResponse response;

        Employee employee = employeeRepository.findWithUserGroupAndPermissionsByUsername(request.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("Ten hoac mat khau khong chinh xac"));

        if (!employee.isActive()) {
            throw new AccountLockedException("Tai khoan bi khoa");
        }
        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            int failCount = employee.getFailedLoginCount() + 1;
            employee.setFailedLoginCount(failCount);

            if (failCount >= 5) {
                employee.setActive(false);
            }
            employeeRepository.save(employee);
            throw new BadCredentialsException("Ten hoac mat khau khong chinh xac. Con " + (5 - failCount) +" lan dang nhap.");

        }


        employee.setFailedLoginCount(0);
        response = new LoginResponse();

        EmployeeDTO employeeDTO = employeeMapper.toDTO(employee);
        response.setEmployeeDTO(employeeDTO);
        statusLogRepository.findFirstByEmployee_UsernameOrderByTimestampDesc( employee.getUsername()).ifPresentOrElse(
                statusLog -> {

            if (statusLog.getStatus().getId() != 1) {
                StatusLog newLog = new StatusLog();
                Status status = cache.getStatus(1);
                log.info("status: {}", status.getName());
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
        log.info(String.format("Login: saved new status log for %s", employee.getUsername()));

        return response;
    }

    @Override
    public void logout(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findWithAllStatusLog(employeeDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Khong ton tai"));

        List<StatusLog> logs = employee.getStatusLogs();

        StatusLog statusLog = logs.get(logs.size()-1);
        if (statusLog.getStatus().getId() != 3) {
            StatusLog newLog = new StatusLog();
            Status status = cache.getStatus(3);
            newLog.setStatus(status);
            newLog.setEmployee(employee);
            employee.getStatusLogs().add(newLog);
            employeeRepository.save(employee);
            log.info(String.format("%s log new status: %s", employeeDTO.getUsername(), status.getName()));

            publisher.publishEvent(new EmployeeEvent(EmployeeEvent.Action.UPDATED,
                    employeeMapper.toDTO(cache.getEmployee(employee.getUsername()))));
        }

        log.info(String.format("%s logout success ", employeeDTO.getUsername()));

    }
}

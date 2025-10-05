package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.dto.LoginRequest;
import com.binhbkfx02295.cshelpdesk.dto.LoginResponse;
import com.binhbkfx02295.cshelpdesk.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.entity.Employee;
import com.binhbkfx02295.cshelpdesk.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.repository.StatusRepository;
import com.binhbkfx02295.cshelpdesk.websocket.event.EmployeeEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountLockedException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final StatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
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

        EmployeeDTO employeeDTO = employeeMapper.toDTO(employee);
        StatusLog statusLog = new StatusLog();
        statusLog.setStatus(statusRepository.getReferenceById(1));
        statusLog.setEmployee(employee);
        employee.getStatusLogs().add(statusLog);

        employeeRepository.save(employee);

        response = new LoginResponse();
        response.setEmployeeDTO(employeeDTO);
        return response;
    }

    @Override
    public void logout(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findWithAllStatusLog(employeeDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Khong ton tai"));

        StatusLog newStatusLog = new StatusLog();
        newStatusLog.setEmployee(employee);
        newStatusLog.setStatus(statusRepository.getReferenceById(3));

        employee.getStatusLogs().add(newStatusLog);

        employee = employeeRepository.save(employee);
        publisher.publishEvent(new EmployeeEvent(EmployeeEvent.Action.UPDATED,
                employeeMapper.toDTO(employee)));
    }
}

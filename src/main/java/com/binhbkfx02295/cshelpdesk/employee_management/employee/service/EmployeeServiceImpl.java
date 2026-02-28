package com.binhbkfx02295.cshelpdesk.employee_management.employee.service;

import com.binhbkfx02295.cshelpdesk.authentication.dto.UserPrincipal;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.*;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeDetailDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.StatusLogMapper;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Status;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroupRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PasswordValidator;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.entity.Ticket;
import com.binhbkfx02295.cshelpdesk.websocket.event.EmployeeEvent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final UserGroupRepository userGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final MasterDataCache cache;
    private final EmployeeMapper mapper;
    private final StatusLogMapper statusLogMapper;
    private final ApplicationEventPublisher publisher;
    private final EntityManager entityManager;

    @Override
    public APIResultSet<EmployeeDTO> createUser(EmployeeDTO employeeDTO) {
        APIResultSet<EmployeeDTO> result;
        if (cache.getEmployee(employeeDTO.getUsername()) != null) {
            result = APIResultSet.badRequest(MSG_ERROR_USERNAME_EXISTED);
        } else if (cache.getUserGroup(employeeDTO.getUserGroup().getGroupId()) == null){
            result = APIResultSet.badRequest(MSG_ERROR_INVALID_USERGROUP);
        } else if (PasswordValidator.validate(employeeDTO.getPassword()) != null){
            result = APIResultSet.badRequest(PasswordValidator.VALIDATION_ERROR);
        } else {
            try {
                Employee user = mapper.toEntity(employeeDTO);
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                //add status log
                user.setUserGroup(userGroupRepository.getReferenceById(employeeDTO.getUserGroup().getGroupId()));
                Status status = cache.getStatus(3);
                StatusLog newLog = new StatusLog();
                newLog.setStatus(status);
                newLog.setEmployee(user);
                user.getStatusLogs().add(newLog);

                user = employeeRepository.save(user);
                entityManager.flush();
                entityManager.clear();
                cache.updateEmployee(user);
                result = APIResultSet.ok(MSG_SUCCESS_CREATE_USER, mapper.toDTO(user));

            } catch (Exception e) {
                log.error("Error message: ", e);
                result = APIResultSet.internalError(MSG_ERROR_CREATE_USER);
            }
        }
        return result;
    }

    @Override
    public APIResultSet<EmployeeDetailDTO> updateUser(String username, EmployeeDTO employeeDTO) {
        APIResultSet<EmployeeDetailDTO> result;
        Employee user = cache.getEmployee(username);
        if (user == null) {
            result = APIResultSet.notFound(MSG_ERROR_EMPLOYEE_NOT_EXIST);
        } else {
            try {
                if (cache.getUserGroup(user.getUserGroup().getGroupId()) == null) {
                    return APIResultSet.badRequest(MSG_ERROR_USERGROUP_NOT_EXISTS);
                }
                mapper.mergeToUser(user, employeeDTO);
                Employee saved = employeeRepository.save(user);
                entityManager.flush();
                entityManager.clear();
                cache.updateEmployee(saved);
                result = APIResultSet.ok(MSG_SUCCESS_UPDATE_USER, mapper.toDetailDTO(cache.getEmployee(employeeDTO.getUsername())));
            } catch (Exception e) {
                log.error("Error message: ", e);
                result =  APIResultSet.internalError(MSG_ERROR_UPDATE_USER);
            }
        }
        return result;
    }

    @Override
    public APIResultSet<EmployeeDTO> getUserByUsername(String username) {
        APIResultSet<EmployeeDTO> result;
        Employee employee = cache.getEmployee(username);
        if (employee == null) {
            result = APIResultSet.notFound(MSG_ERROR_GET_EMPLOYEE);
        } else {
            result = APIResultSet.ok(MSG_SUCCESS_GET_EMPLOYEE, mapper.toDTO(employee));
        }

        return result;
    }

    @Override
    public APIResultSet<List<EmployeeDTO>> getAllUsers() {
        try {
            List<Employee> result = cache.getAllEmployees().values().stream().toList();
            return APIResultSet.ok(MSG_SUCCESS_GET_ALL_EMPLOYEES, result.stream().map(mapper::toDTO).toList());
        } catch (Exception e) {
            log.error("Error message: ", e);
            return APIResultSet.internalError();
        }
    }

    @Override
    public APIResultSet<Void> changePassword(String username, String password, String newPassword) {
        try {
            Optional<Employee> userOpt = employeeRepository.findById(username);
            if (userOpt.isEmpty()) return APIResultSet.badRequest(MSG_ERROR_WRONG_PASSWORD);

            Employee user = userOpt.get();
            //validate password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return APIResultSet.badRequest(MSG_ERROR_WRONG_PASSWORD);
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            employeeRepository.save(user);
            return APIResultSet.ok(MSG_OK_RESET_PASSWORD, null);
        } catch (Exception e) {
            log.error("Error message: ", e);
            return APIResultSet.internalError();
        }
    }

    @Override
    public APIResultSet<List<EmployeeDashboardDTO>> getForDashboard() {
        APIResultSet<List<EmployeeDashboardDTO>> result;
        try {
            List<Ticket> tickets = cache.getDashboardTickets().values().stream().toList();
            Map<String, EmployeeDashboardDTO> list = cache.getAllEmployees().values().stream().map(
                    mapper::toDashboardDTO).collect(Collectors.toMap(EmployeeDashboardDTO::getUsername, Function.identity()));
            for (Ticket ticket : tickets) {
                if (ticket.getAssignee() != null) {
                    EmployeeDashboardDTO target = list.get(ticket.getAssignee().getUsername());
                    if (ticket.getProgressStatus().getId() != 3) {
                        target.setTicketCount(target.getTicketCount() + 1);
                    }
                }

            }
            result = APIResultSet.ok(MSG_SUCCESS_GET_ALL_EMPLOYEES, list.values().stream().toList());
        } catch (Exception e) {
            log.error("Error message: ", e);
            result =  APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<StatusLogDTO> getLatestOnlineStatus(String username) {
        APIResultSet<StatusLogDTO> result;
        try {
            StatusLog statusLog = cache.getEmployee(username).getStatusLogs().get(
                    cache.getEmployee(username).getStatusLogs().size() - 1
            );
            result = APIResultSet.ok(MSG_SUCCESS_GET_STATUS, statusLogMapper.toDTO(statusLog));
        } catch (Exception e) {
            log.error("Error message: ", e);
            return APIResultSet.internalError();
        }
        return result;
    }


    @Override
    public APIResultSet<Void> updateOnlineStatus(StatusLogDTO logDTO) {
        try {
            Optional<Employee> employeeOtp = employeeRepository.findWithTop1StatusLog(logDTO.getUsername());
            StatusLog latestLog;
            if (employeeOtp.isPresent()) {
                Employee employee = employeeOtp.get();
                latestLog = employee.getStatusLogs().get(0);
                if (latestLog.getStatus().getId() != (logDTO.getStatus().getId())) {
                    StatusLog temp = statusLogMapper.toEntity(logDTO);
                    employee.getStatusLogs().add(temp);
                    employeeRepository.save(employee);
                    entityManager.flush();
                    entityManager.clear();
                    cache.updateAllEmployees();
                    publisher.publishEvent(new EmployeeEvent(EmployeeEvent.Action.UPDATED, mapper.toDashboardDTO(cache.getEmployee(logDTO.getUsername()))));
                    return APIResultSet.ok(MSG_SUCCESS_UPDATE_ONLINE_STATUS, null);
                } else {
                    return APIResultSet.badRequest(MSG_ERROR_UPDATE_ONLINE_STATUS);
                }
            } else {
                return APIResultSet.badRequest(MSG_ERROR_EMPLOYEE_NOT_EXIST, null);
            }
        } catch (Exception e) {
            log.error("Error message: ", e);
            return APIResultSet.internalError();
        }
    }

    @Override
    public APIResultSet<Void> deleteByUsername(String username) {
        try {
            if (!employeeRepository.existsByUsername(username)) {
                return APIResultSet.badRequest(MSG_ERROR_EMPLOYEE_NOT_EXIST);
            }
            employeeRepository.deleteByUsername(username);
            entityManager.flush();
            entityManager.clear();
            cache.getAllEmployees().remove(username);
            return APIResultSet.ok(MSG_SUCCESS_DELETE_EMPLOYEE, null);
        } catch (Exception e) {
            log.error("Error message: ", e);
            return APIResultSet.internalError();
        }

    }

    @Override
    public APIResultSet<Void> resetPassword(ResetPasswordDTO resetPasswordDTO) {
        String validationErrorString = PasswordValidator.validate(resetPasswordDTO.getDefaultPassword());
        APIResultSet<Void> result;
        try {
            if (validationErrorString != null) {
                result = APIResultSet.badRequest(validationErrorString);
            } else {
                Optional<Employee> employeeOpt = employeeRepository.findById(resetPasswordDTO.getUsername());
                if (employeeOpt.isPresent()) {
                    employeeOpt.get().setPassword(passwordEncoder.encode(resetPasswordDTO.getDefaultPassword()));
                    employeeRepository.save(employeeOpt.get());
                    result = APIResultSet.ok(MSG_SUCCESS_RESET_PASSWORD, null);
                } else {
                    result = APIResultSet.badRequest(MSG_ERROR_RESET_PASSWORD);
                }
            }
        } catch (Exception e) {
            log.error("Error message: ", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findWithUserGroupAndPermissionsByUsername(username)
                .map(e -> new UserPrincipal(
                        e.getUsername(),
                        e.getName(),
                        e.getDescription(),
                        e.getUserGroup().getPermissions()
                                .stream()
                                .map(p -> new SimpleGrantedAuthority("ROLE_" + p.getName()))
                                .toList(),
                        e))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}

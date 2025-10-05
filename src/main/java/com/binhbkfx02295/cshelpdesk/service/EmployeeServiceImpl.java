package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.*;
import com.binhbkfx02295.cshelpdesk.dto.EmployeeDetailDTO;
import com.binhbkfx02295.cshelpdesk.dto.StatusLogMapper;
import com.binhbkfx02295.cshelpdesk.repository.StatusLogRepository;
import com.binhbkfx02295.cshelpdesk.repository.StatusRepository;
import com.binhbkfx02295.cshelpdesk.entity.UserGroup;
import com.binhbkfx02295.cshelpdesk.entity.Employee;
import com.binhbkfx02295.cshelpdesk.entity.Status;
import com.binhbkfx02295.cshelpdesk.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.dto.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.repository.UserGroupRepository;
import com.binhbkfx02295.cshelpdesk.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.entity.Ticket;
import com.binhbkfx02295.cshelpdesk.repository.TicketRepository;
import com.binhbkfx02295.cshelpdesk.websocket.event.EmployeeEvent;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final StatusRepository statusRepository;
    private final TicketRepository ticketRepository;
    private final EmployeeRepository employeeRepository;
    private final UserGroupRepository userGroupRepository;
    private final StatusLogRepository statusLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper mapper;
    private final StatusLogMapper statusLogMapper;
    private final ApplicationEventPublisher publisher;
    private final EntityManager entityManager;

    @Override
    public EmployeeDTO createUser(EmployeeDTO employeeDTO) {
        EmployeeDTO result;
        if (employeeRepository.existsByUsername(employeeDTO.getUsername()))
            throw new EntityExistsException("Tên tài khoản đã tồn tại");

        UserGroup userGroup = userGroupRepository.findById(employeeDTO.getUserGroup().getGroupId())
                .orElseThrow(()->new EntityNotFoundException("Nhóm người dùng không hợp lệ"));

        Employee user = mapper.toEntity(employeeDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //add status log
        user.setUserGroup(userGroupRepository.getReferenceById(employeeDTO.getUserGroup().getGroupId()));
        Status status = statusRepository.getReferenceById(3);
        StatusLog newLog = new StatusLog();
        newLog.setStatus(status);
        newLog.setEmployee(user);
        user.getStatusLogs().add(newLog);
        return mapper.toDTO(employeeRepository.save(user));
    }

    @Override
    public EmployeeDetailDTO updateUser(String username, EmployeeDTO dto) {
        Employee user = employeeRepository.findByUsername(username)
                .orElseThrow(()->new EntityNotFoundException("Update user: user not found"));

        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setDescription(dto.getDescription());
        user.setActive(dto.isActive());
        user.setName(dto.getName());
        user.setFailedLoginCount(dto.getFailedLoginCount());
        user.setUserGroup(
                dto.getUserGroup() == null ? null :
                        userGroupRepository.getReferenceById(dto.getUserGroup().getGroupId())
        );

        user.setStatusLogs(dto.getStatusLogs().isEmpty() ? new ArrayList<>() :
                dto.getStatusLogs()
                        .stream()
                        .map(s -> statusLogRepository.getReferenceById(s.getStatus().getId()))
                        .toList());

        return mapper.toDetailDTO(employeeRepository.save(user));
    }

    @Override
    public EmployeeDTO getUserByUsername(String username) {
        return employeeRepository.findById(username)
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Tên tài khoản khong tồn tại"));
    }

    @Override
    public List<EmployeeDTO> getAllUsers() {
        return employeeRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void changePassword(String username, String password, String newPassword) {
        Employee employee = getEmployee(username);

        if (passwordEncoder.matches(password, employee.getPassword())) {
            throw new IllegalArgumentException("Mat khau cu khong khop.");
        }

        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
    }

    private Employee getEmployee(String username) {
        return employeeRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("Tên tài khoản khong tồn tại"));
    }


    @Override
    public List<EmployeeDashboardDTO> getForDashboard() {

        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.atStartOfDay();
        LocalDateTime endDateTime = today.plusDays(1).atStartOfDay().minusNanos(1);
        Timestamp startOfDay = Timestamp.valueOf(startDateTime);
        Timestamp endOfDay = Timestamp.valueOf(endDateTime);
        List<Ticket> tickets = ticketRepository.findOpeningOrToday(startOfDay, endOfDay);

        Map<String, EmployeeDashboardDTO> employees = employeeRepository.findAll()
                .stream()
                .map(mapper::toDashboardDTO)
                .collect(Collectors.toMap(EmployeeDashboardDTO::getUsername, Function.identity()));

        for (Ticket ticket: tickets) {
            if (ticket.getAssignee() != null && ticket.getProgressStatus().getId() != 3) {
                EmployeeDashboardDTO employee = employees.get(ticket.getAssignee().getUsername());
                employee.setTicketCount(employee.getTicketCount() + 1);
            }
        }
        return employees.values().stream().toList();
    }

    @Override
    public StatusLogDTO getLatestOnlineStatus(String username) {
        return statusLogRepository.findFirstByEmployee_UsernameOrderByTimestampDesc(username)
                .map(statusLogMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Tên tài khoản khong tồn tại"));
    }


    @Override
    public List<StatusLogDTO> findWithAllLogs(EmployeeDTO employeeDTO) {
        return statusLogRepository.findAllByEmployee_Username(employeeDTO.getUsername())
                .stream()
                .map(statusLogMapper::toDTO)
                .toList();
    }

    @Override
    public void updateOnlineStatus(StatusLogDTO logDTO) {

        Employee employee = employeeRepository.findById(logDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Tên tài khoản khong tồn tại"));

        StatusLog statusLog = employee.getStatusLogs().get(employee.getStatusLogs().size()-1);

        if (statusLog.getStatus().getId() == logDTO.getStatus().getId())
            throw new IllegalArgumentException("Trùng status, không cập nhật");

        StatusLog newStatusLog = new StatusLog();
        newStatusLog.setStatus(statusRepository.getReferenceById(logDTO.getStatus().getId()));
        newStatusLog.setEmployee(employee);

        employee.getStatusLogs().add(newStatusLog);
        employeeRepository.save(employee);
        publisher.publishEvent(new EmployeeEvent(EmployeeEvent.Action.UPDATED, mapper.toDTO(employee)));
    }


    public List<StatusDTO> getAllOnlineStatus() {
        return statusRepository.findAll()
                .stream()
                .map(status -> {
                    StatusDTO statusDTO = new StatusDTO();
                    statusDTO.setId(status.getId());
                    statusDTO.setName(status.getName());
                    return statusDTO;
                })
                .toList();
    }

    @Override
    public void deleteByUsername(String username) {
        if (!employeeRepository.existsByUsername(username))
            throw new EntityNotFoundException("Tên tài khoản khong tồn tại");

        employeeRepository.deleteByUsername(username);
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Employee employee = employeeRepository.findById(resetPasswordDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Tên tài khoản khong tồn tại"));

        employee.setPassword(passwordEncoder.encode(resetPasswordDTO.getDefaultPassword()));
        employeeRepository.save(employee);
    }
}

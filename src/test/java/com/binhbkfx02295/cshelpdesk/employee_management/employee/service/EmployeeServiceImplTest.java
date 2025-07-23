package com.binhbkfx02295.cshelpdesk.employee_management.employee.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDashboardDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.StatusLogDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Status;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeDetailDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.StatusLogMapper;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroup;
import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroupDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroupRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PasswordValidator;
import jakarta.persistence.EntityManager;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock EmployeeRepository employeeRepository;
    @Mock UserGroupRepository userGroupRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock MasterDataCache cache;
    @Mock EmployeeMapper mapper;
    @Mock StatusLogMapper statusLogMapper;
    @Mock ApplicationEventPublisher publisher;
    @Mock EntityManager entityManager;

    @InjectMocks EmployeeServiceImpl employeeService;

    @Test
    void testCreateUser_username_validation_failed_failed() {
        EmployeeDTO employeeDTO = mockEmployeeDTO();

        when(cache.getEmployee(any())).thenReturn(new Employee());
        APIResultSet<EmployeeDTO> result = employeeService.createUser(employeeDTO);
        assertEquals(400, result.getHttpCode());
        assertEquals(EmployeeService.MSG_ERROR_USERNAME_EXISTED,result.getMessage());
    }

    @Test
    void testCreateUser_userGroup_null_failed() {
        //TODO:
        EmployeeDTO employeeDTO = mockEmployeeDTO();
        when(cache.getEmployee(any())).thenReturn(null);
        when(cache.getUserGroup(anyInt())).thenReturn(null);
        APIResultSet<EmployeeDTO> result = employeeService.createUser(employeeDTO);
        assertEquals(400, result.getHttpCode());
        assertEquals(EmployeeService.MSG_ERROR_INVALID_USERGROUP,result.getMessage());
    }

    @Test
    void testCreateUser_password_validation_failed_failed() {
        EmployeeDTO employeeDTO = mockEmployeeDTO();
        employeeDTO.setPassword("1");
        when(cache.getEmployee(any())).thenReturn(null);
        when(cache.getUserGroup(anyInt())).thenReturn(new UserGroup());
        APIResultSet<EmployeeDTO> result = employeeService.createUser(employeeDTO);
        assertEquals(400, result.getHttpCode());
        assertEquals(PasswordValidator.VALIDATION_ERROR,result.getMessage());
    }

    @Test
    void testCreateUser_internalError_success() {
        EmployeeDTO employeeDTO = mockEmployeeDTO();
        when(cache.getEmployee(any())).thenReturn(null);
        when(cache.getUserGroup(anyInt())).thenReturn(new UserGroup());
        when(mapper.toEntity(employeeDTO)).thenThrow(new RuntimeException());

        APIResultSet<EmployeeDTO> result = employeeService.createUser(employeeDTO);
        assertEquals(500, result.getHttpCode());
        assertEquals(EmployeeService.MSG_ERROR_CREATE_USER,result.getMessage());
    }

    @Test
    void testCreateUser_success() {
        EmployeeDTO employeeDTO = mockEmployeeDTO();
        when(cache.getEmployee(any())).thenReturn(null);
        when(cache.getUserGroup(anyInt())).thenReturn(new UserGroup());
        when(mapper.toEntity(employeeDTO)).thenReturn(new Employee());
        when(userGroupRepository.getReferenceById(anyInt())).thenReturn(new UserGroup());
        when(cache.getStatus(3)).thenReturn(new Status());
        when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());
        APIResultSet<EmployeeDTO> result = employeeService.createUser(employeeDTO);
        assertEquals(200, result.getHttpCode());
        assertEquals(EmployeeService.MSG_SUCCESS_CREATE_USER,result.getMessage());
    }


    @Test
    void testUpdateUser_username_not_exists_failed() {
        EmployeeDTO employeeDTO = mockEmployeeDTO();

        when(cache.getEmployee(anyString())).thenReturn(null);
        APIResultSet<EmployeeDetailDTO> result = employeeService.updateUser(employeeDTO.getUsername(), employeeDTO);
        assertEquals(404, result.getHttpCode());
        assertEquals(EmployeeService.MSG_ERROR_EMPLOYEE_NOT_EXIST,result.getMessage());
    }

    @Test
    void testUpdateUser_internalError_failed() {
        EmployeeDTO employeeDTO = mockEmployeeDTO();
        when(cache.getEmployee(anyString())).thenReturn(Employee.builder().userGroup(new UserGroup()).build());
        when(cache.getUserGroup(anyInt())).thenReturn(new UserGroup());
        when(employeeRepository.save(any(Employee.class))).thenThrow(new RuntimeException());
        APIResultSet<EmployeeDetailDTO> result = employeeService.updateUser(employeeDTO.getUsername(), employeeDTO);
        assertEquals(500, result.getHttpCode());
        assertEquals(EmployeeService.MSG_ERROR_UPDATE_USER,result.getMessage());
    }

    @Test
    void testUpdateUser_success() {
        EmployeeDTO employeeDTO = mockEmployeeDTO();

        when(cache.getEmployee(anyString())).thenReturn(Employee.builder().userGroup(new UserGroup()).build());
        when(cache.getUserGroup(anyInt())).thenReturn(new UserGroup());
        when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());
        APIResultSet<EmployeeDetailDTO> result = employeeService.updateUser(employeeDTO.getUsername(), employeeDTO);
        assertEquals(200, result.getHttpCode());
        assertEquals(EmployeeService.MSG_SUCCESS_UPDATE_USER,result.getMessage());
    }

    @Test
    void testGetUserByUsername_username_not_found_failed() {
        when(cache.getEmployee(anyString())).thenReturn(null);
        APIResultSet<EmployeeDTO> result = employeeService.getUserByUsername("any");
        assertEquals(404, result.getHttpCode());
        assertEquals(EmployeeService.MSG_ERROR_GET_EMPLOYEE,result.getMessage());

    }

    @Test
    void testGetUserByUsername_username_found_success() {
        when(cache.getEmployee(anyString())).thenReturn(new Employee());
        when(mapper.toDTO(any(Employee.class))).thenReturn(new EmployeeDTO());
        APIResultSet<EmployeeDTO> result = employeeService.getUserByUsername("any");
        assertEquals(200, result.getHttpCode());
        assertEquals(EmployeeService.MSG_SUCCESS_GET_EMPLOYEE,result.getMessage());
    }
    @Test
    void testGetAllUser_internalError_failed() {
        when(cache.getAllEmployees()).thenThrow(new RuntimeException());
        APIResultSet<List<EmployeeDTO>> result = employeeService.getAllUsers();
        assertEquals(500, result.getHttpCode());
        assertEquals(APIResultSet.MSG_INTERNAL_ERROR,result.getMessage());
    }

    @Test
    void testGetAllUser_success() {
        when(cache.getAllEmployees()).thenReturn(new HashMap<>());
        APIResultSet<List<EmployeeDTO>> result = employeeService.getAllUsers();
        assertEquals(200, result.getHttpCode());
        assertEquals(EmployeeService.MSG_SUCCESS_GET_ALL_EMPLOYEES,result.getMessage());
    }

    @Test
    void testChangePassword_internalError_failed() {
        when(employeeRepository.findById(anyString())).thenThrow(new RuntimeException());
        EmployeeDTO employeeDTO = mockEmployeeDTO();
        APIResultSet<Void> result = employeeService.changePassword(employeeDTO.getUsername(), employeeDTO.getPassword(), "aa");
        assertEquals(500, result.getHttpCode());
        assertEquals(APIResultSet.MSG_INTERNAL_ERROR,result.getMessage());
    }

    @Test
    void testChangePassword_wrong_password_failed() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(new Employee()));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        EmployeeDTO employeeDTO = mockEmployeeDTO();
        APIResultSet<Void> result = employeeService.changePassword(employeeDTO.getUsername(), employeeDTO.getPassword(), "aa");
        assertEquals(400, result.getHttpCode());
        assertEquals(EmployeeService.MSG_ERROR_WRONG_PASSWORD,result.getMessage());
    }

    @Test
    void testChangePassword_success() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(new Employee()));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());
        EmployeeDTO employeeDTO = mockEmployeeDTO();
        APIResultSet<Void> result = employeeService.changePassword(employeeDTO.getUsername(), employeeDTO.getPassword(), "aa");
        assertEquals(200, result.getHttpCode());
        assertEquals(EmployeeService.MSG_OK_RESET_PASSWORD,result.getMessage());
    }

    @Test
    void testGetForDashboard_internal_error() {
        when(cache.getDashboardTickets()).thenThrow(new RuntimeException());
        APIResultSet<List<EmployeeDashboardDTO>> result = employeeService.getForDashboard();
        assertEquals(500, result.getHttpCode());
        assertEquals(APIResultSet.MSG_INTERNAL_ERROR,result.getMessage());
    }

    @Test
    void testGetForDashboard_success() {

        when(cache.getDashboardTickets()).thenReturn(new HashMap<>());
        when(cache.getAllEmployees()).thenReturn(new HashMap<>());
        APIResultSet<List<EmployeeDashboardDTO>> result = employeeService.getForDashboard();
        assertEquals(200, result.getHttpCode());
        assertEquals(EmployeeService.MSG_SUCCESS_GET_ALL_EMPLOYEES,result.getMessage());

    }

    EmployeeDTO mockEmployeeDTO() {
        return EmployeeDTO.builder()
                .username("test")
                .password("Abcd@1234")
                .userGroup(UserGroupDTO.builder().groupId(1).code("staff").build())
                .build();
    }


}
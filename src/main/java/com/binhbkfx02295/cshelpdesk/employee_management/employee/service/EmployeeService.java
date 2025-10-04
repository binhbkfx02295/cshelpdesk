package com.binhbkfx02295.cshelpdesk.employee_management.employee.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.*;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeDetailDTO;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createUser(EmployeeDTO employeeDTO);
    EmployeeDetailDTO updateUser(String username, EmployeeDTO employee);
    EmployeeDTO getUserByUsername(String username);
    List<EmployeeDTO> getAllUsers();
    void changePassword(String username, String password, String newPassword);
    List<EmployeeDashboardDTO> getForDashboard();
    StatusLogDTO getLatestOnlineStatus(String username);
    List<StatusLogDTO> findWithAllLogs(EmployeeDTO employeeDTO);
    void updateOnlineStatus(StatusLogDTO logDTO);
    List<StatusDTO> getAllOnlineStatus();
    void deleteByUsername(String testaccount);
    void resetPassword(ResetPasswordDTO resetPasswordDTO);
}

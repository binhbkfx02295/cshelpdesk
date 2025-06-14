package com.binhbkfx02295.cshelpdesk.employee_management.employee.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.*;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeDetailDTO;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import java.util.List;

public interface EmployeeService {
    APIResultSet<EmployeeDTO> createUser(EmployeeDTO employeeDTO);
    APIResultSet<EmployeeDetailDTO> updateUser(String username, EmployeeDTO employee);
    APIResultSet<EmployeeDTO> getUserByUsername(String username);
    APIResultSet<List<EmployeeDTO>> getAllUsers();
    APIResultSet<Void> changePassword(String username, String password, String newPassword);
    APIResultSet<Void> existsByUsername(String username);
    APIResultSet<List<EmployeeDashboardDTO>> getForDashboard();
    APIResultSet<StatusLogDTO> getLatestOnlineStatus(String username);
    APIResultSet<List<StatusLogDTO>> findWithAllLogs(EmployeeDTO employeeDTO);
    APIResultSet<Void> updateOnlineStatus(StatusLogDTO logDTO);
    APIResultSet<List<StatusDTO>> getAllOnlineStatus();
    APIResultSet<Void> deleteByUsername(String testaccount);
    APIResultSet<Void> resetPassword(ResetPasswordDTO resetPasswordDTO);
}

package com.binhbkfx02295.cshelpdesk.employee_management.authentication.dto;

import com.binhbkfx02295.cshelpdesk.employee_management.authentication.util.ValidationResult;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDTO;
import lombok.Data;

@Data
public class LoginResponse {
    private EmployeeDTO employeeDTO;
    private ValidationResult validationResult;
}
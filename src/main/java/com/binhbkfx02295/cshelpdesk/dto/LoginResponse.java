package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.employee_management.authentication.util.ValidationResult;
import lombok.Data;

@Data
public class LoginResponse {
    private EmployeeDTO employeeDTO;
    private ValidationResult validationResult;
}
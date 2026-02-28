package com.binhbkfx02295.cshelpdesk.authentication.dto;

import com.binhbkfx02295.cshelpdesk.authentication.util.ValidationResult;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private EmployeeDTO employeeDTO;
    private ValidationResult validationResult;
}
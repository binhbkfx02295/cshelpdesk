package com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper;

import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroupDTO;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class EmployeeDetailDTO {
    private UserGroupDTO userGroup;
    private String name;
    private String username;
    private String password;
    private String description;
    private String email;
    private String phone;
    private Timestamp createdAt;
    private boolean isActive;
    private int failedLoginCount;
}

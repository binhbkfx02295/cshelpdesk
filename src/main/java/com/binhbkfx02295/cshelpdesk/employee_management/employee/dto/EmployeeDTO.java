package com.binhbkfx02295.cshelpdesk.employee_management.employee.dto;

import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
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
    private List<StatusLogDTO> statusLogs = new ArrayList<>();

}

package com.binhbkfx02295.cshelpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDashboardDTO {
    private String username;
    private String name;
    private String description;
    private StatusLogDTO statusLog;
    private UserGroupDTO userGroup;
    private int ticketCount;
}

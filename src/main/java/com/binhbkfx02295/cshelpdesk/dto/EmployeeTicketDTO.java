package com.binhbkfx02295.cshelpdesk.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class EmployeeTicketDTO {
    private String username;
    private String name;
    private UserGroupDTO group;
}

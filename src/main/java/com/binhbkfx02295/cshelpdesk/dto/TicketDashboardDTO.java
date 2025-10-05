package com.binhbkfx02295.cshelpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDashboardDTO {

    private int id;
    private String title;
    private Timestamp createdAt;
    private EmployeeTicketDTO assignee;
    private FacebookUserListDTO facebookUser;
    private ProgressStatusDTO progressStatus;
    private boolean hasNewMessage;

}

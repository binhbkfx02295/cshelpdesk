package com.binhbkfx02295.cshelpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketListDTO {
    private int id;
    private String title;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp closedAt;

    private ProgressStatusDTO progressStatus;
    private CategoryDTO category;
    private EmployeeDTO assignee;
    private FacebookUserListDTO facebookUser;
    private EmotionDTO emotion;
    private SatisfactionDTO satisfaction;
}

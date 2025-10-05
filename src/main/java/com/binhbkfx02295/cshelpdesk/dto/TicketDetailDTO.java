package com.binhbkfx02295.cshelpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailDTO {
    private int id;
    private String title;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp closedAt;
    private ProgressStatusDTO progressStatus;
    private CategoryDTO category;
    private EmployeeDTO assignee;
    private EmotionDTO emotion;
    private SatisfactionDTO satisfaction;
    private FacebookUserListDTO facebookUser;
    private List<TagDTO> tags = new ArrayList<>();
    private List<NoteDTO> notes = new ArrayList<>();
}

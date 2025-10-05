package com.binhbkfx02295.cshelpdesk.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class MessageEventDTO {
    public int id;
    public TicketDashboardDTO ticket;
    public String text;
    public boolean senderEmployee;
    public Timestamp timestamp;
    public boolean senderSystem;
    public List<AttachmentDTO> attachments = new ArrayList<>();
}

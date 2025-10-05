package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.Note;
import com.binhbkfx02295.cshelpdesk.entity.Ticket;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class NoteDTO {
    private int id;
    private String text;
    private int ticketId;
    private Timestamp timestamp;

    public Note toEntity() {
        Note note = new Note();
        note.setId(id);
        note.setText(text);
        note.setTimestamp(timestamp);
        if (ticketId != 0) {
            Ticket ticket = new Ticket();
            ticket.setId(ticketId);
            note.setTicket(ticket);
        }
        return note;
    }
}

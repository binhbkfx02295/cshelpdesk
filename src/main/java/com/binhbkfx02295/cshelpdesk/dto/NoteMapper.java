package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.Note;
import com.binhbkfx02295.cshelpdesk.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteDTO toDTO(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setText(note.getText());
        dto.setTimestamp(note.getTimestamp());
        dto.setTicketId(note.getTicket().getId());
        return dto;
    };

    public Note toEntity(NoteDTO dto) {
        Note entity = new Note();
        entity.setId(dto.getId());
        entity.setText(dto.getText());
        entity.setTimestamp(dto.getTimestamp());

        if (dto.getTicketId() != 0) {
            Ticket ticket = new Ticket();
            ticket.setId(0);
            entity.setTicket(ticket);
        }

        return entity;
    }
}

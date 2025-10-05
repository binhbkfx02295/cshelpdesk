package com.binhbkfx02295.cshelpdesk.entity;

import com.binhbkfx02295.cshelpdesk.dto.NoteDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 1024)
    private String text;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column
    @CreationTimestamp
    private Timestamp timestamp;

    public NoteDTO toDTO() {
        NoteDTO dto = new NoteDTO();
        dto.setId(id);
        dto.setText(text);
        dto.setTimestamp(timestamp);
        return dto;

    }
}


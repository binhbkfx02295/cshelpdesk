package com.binhbkfx02295.cshelpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private Timestamp timestamp;

    private boolean senderEmployee;

    private boolean senderSystem;

    @ManyToOne
    private Ticket ticket;

    @Column(length = 1024)
    private String text = "";

    @OneToMany(mappedBy = "message", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

}

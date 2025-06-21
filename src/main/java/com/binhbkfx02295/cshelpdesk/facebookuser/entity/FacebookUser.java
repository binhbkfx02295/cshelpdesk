package com.binhbkfx02295.cshelpdesk.facebookuser.entity;

import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "facebook_user")
public class FacebookUser {
    @Id
    @Column(nullable = false)
    private String facebookId;

    @Column(nullable = false)
    private String facebookName;

    @Column(nullable = false)
    private String facebookProfilePic;
    private String realName;
    private String email;
    private String phone;
    private String zalo;

    private Instant createdAt;

    @OneToMany(mappedBy = "facebookUser")
    private List<Ticket> tickets = new ArrayList<>();

}

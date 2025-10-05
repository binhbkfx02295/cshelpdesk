package com.binhbkfx02295.cshelpdesk.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;


    // Người xử lý (username hoặc tên người dùng)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee assignee;

    // khách hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facebook_user_id", nullable = false)
    private FacebookUser facebookUser;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_status_id", nullable = false)
    private ProgressStatus progressStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id")
    private Emotion emotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "satisfaction_id")
    private Satisfaction satisfaction;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ticket_tag",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();


    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp lastUpdateAt;

    private Timestamp closedAt;

    private Long firstResponseRate;
    private Long overallResponseRate;
    private Long resolutionRate;
    private int tokenUsed;
    private float price;
    private String gptModelUsed;

}

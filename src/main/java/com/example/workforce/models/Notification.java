package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.workforce.models.keys.NotificationId;

@Entity
@Table(name = "Notification")
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor
public class Notification {

    @EmbeddedId
    private NotificationId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MemberId")
    private Member member;

    @Column(name = "timestamps")
    private LocalDateTime timestamp;

    @Column(name = "View_Time")
    private LocalDateTime viewTime;

    @Column(name = "message")
    private String message;

    @Column(name = "title")
    private String title;
}

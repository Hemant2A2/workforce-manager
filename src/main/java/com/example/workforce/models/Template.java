package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Template")
@Data
@NoArgsConstructor
public class Template {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "templateId")
    private Integer id;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator")
    private Member creator;
}

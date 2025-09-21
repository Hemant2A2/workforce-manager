package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Week")
@Data
@NoArgsConstructor
public class Week {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekId")
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
}

package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Shift")
@Data
@NoArgsConstructor
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Shift_Id")
    private Long id;

    @Column(name = "Day") 
    private String day;

    @Column(name = "start_time") 
    private LocalTime startTime;

    @Column(name = "end_time") 
    private LocalTime endTime;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "location_Id", nullable = false)
    private Location location;

    @ManyToMany(mappedBy = "unavailableShifts")
    private Set<Member> unavailableMembers = new HashSet<>();
}

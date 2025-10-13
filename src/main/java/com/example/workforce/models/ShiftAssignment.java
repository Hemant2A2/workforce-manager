package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;

import com.example.workforce.enums.Attendance;
import com.example.workforce.models.keys.ShiftAssignmentId;

@Entity
@Table(name = "Shift_Assignment")
@Data
@NoArgsConstructor
public class ShiftAssignment {
    @EmbeddedId
    private ShiftAssignmentId id; // (shift, week, emp , role)

    @Column(name = "Attendance")
    @Enumerated(EnumType.STRING)
    private Attendance attendance; // "PRESENT"/"ABSENT"/"LEAVE"/"SCHEDULED"

    @MapsId("shiftId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ShiftId")
    private Shift shift;

    @MapsId("empId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmpId")
    private Member member;

    @MapsId("weekId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Week_Id")
    private Week week;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Role_Id")
    private Role role;
}

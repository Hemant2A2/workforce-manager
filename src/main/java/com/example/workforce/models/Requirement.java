package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;
import com.example.workforce.models.keys.RequirementId;

@Entity
@Table(name = "Requirement")
@Data
@NoArgsConstructor
public class Requirement {
    @EmbeddedId
    private RequirementId id; // (role id, shift id)

    @Column(name = "count", nullable = false)
    private Integer count;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_Id")
    private Role role;

    @MapsId("shiftId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Shift_Id")
    private Shift shift;
}

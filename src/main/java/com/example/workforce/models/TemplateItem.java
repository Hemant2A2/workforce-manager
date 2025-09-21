package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;
import com.example.workforce.models.keys.TemplateItemId;

@Entity
@Table(name = "TempItems")
@Data
@NoArgsConstructor
public class TemplateItem {
    @EmbeddedId
    private TemplateItemId id; // (shiftId, templateId, empId, roleId)

    @MapsId("empId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Empid")
    private Member member;

    @MapsId("shiftId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ShiftId")
    private Shift shift;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private Role role;

    @MapsId("templateId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TemplateId")
    private Template template;
}

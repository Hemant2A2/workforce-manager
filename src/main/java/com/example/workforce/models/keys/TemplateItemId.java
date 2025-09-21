package com.example.workforce.models.keys;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TemplateItemId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "Empid")
    private Long empId;

    @Column(name = "ShiftId")
    private Long shiftId;

    @Column(name = "roleId")
    private Long roleId;

    @Column(name = "TemplateId")
    private Long templateId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateItemId)) return false;
        TemplateItemId that = (TemplateItemId) o;
        return Objects.equals(shiftId, that.shiftId) &&
               Objects.equals(templateId, that.templateId) &&
               Objects.equals(empId, that.empId)&&
               Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shiftId, templateId, empId, roleId);
    }
}

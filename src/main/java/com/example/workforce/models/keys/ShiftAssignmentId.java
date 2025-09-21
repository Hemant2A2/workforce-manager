package com.example.workforce.models.keys;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ShiftAssignmentId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "ShiftId")
    private Long shiftId;

    @Column(name = "EmpId")
    private Long empId;

    @Column(name = "Week_Id")
    private Long weekId;

    @Column(name = "Role_Id")
    private Long roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftAssignmentId)) return false;
        ShiftAssignmentId that = (ShiftAssignmentId) o;
        return Objects.equals(shiftId, that.shiftId) &&
               Objects.equals(weekId, that.weekId) &&
               Objects.equals(empId, that.empId)&&
               Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shiftId, weekId, empId, roleId);
    }
}

package com.example.workforce.models.keys;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LeaveRequestId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "EmpId")
    private Long empId;

    @Column(name = "ShiftId")
    private Long shiftId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaveRequestId)) return false;
        LeaveRequestId that = (LeaveRequestId) o;
        return Objects.equals(empId, that.empId) &&
               Objects.equals(shiftId, that.shiftId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, shiftId);
    }
}

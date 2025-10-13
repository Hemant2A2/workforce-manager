package com.example.workforce.models.keys;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RequirementId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "role_Id")
    private Integer roleId;

    @Column(name = "Shift_Id")
    private Integer shiftId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequirementId)) return false;
        RequirementId that = (RequirementId) o;
        return Objects.equals(roleId, that.roleId) &&
               Objects.equals(shiftId, that.shiftId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, shiftId);
    }
}

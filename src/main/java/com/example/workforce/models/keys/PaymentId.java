package com.example.workforce.models.keys;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "weekId")
    private Integer weekId;

    @Column(name = "memb_Id")
    private Integer memberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentId)) return false;
        PaymentId that = (PaymentId) o;
        return Objects.equals(memberId, that.memberId) &&
               Objects.equals(weekId, that.weekId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, weekId);
    }
}

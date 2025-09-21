package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import com.example.workforce.models.keys.PaymentId;

@Entity
@Table(name = "Payment")
@Data
@NoArgsConstructor
public class Payment {
    @EmbeddedId
    private PaymentId id; // (memb id, week id)

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "unapproved_leave")
    private Integer unapprovedLeaves;

    @MapsId("weekId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekId")
    private Week week;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memb_Id")
    private Member member;
}

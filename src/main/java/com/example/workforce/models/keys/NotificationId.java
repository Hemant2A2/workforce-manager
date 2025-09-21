package com.example.workforce.models.keys;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "MemberId")
    private Long memberId;

    // sequence per member (application / db must set this)
    @Column(name = "notif_seq")
    private Long notifSeq;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationId)) return false;
        NotificationId that = (NotificationId) o;
        return Objects.equals(notifSeq, that.notifSeq) &&
               Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notifSeq, memberId);
    }
}

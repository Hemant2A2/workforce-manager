package com.example.workforce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.workforce.models.Notification;
import com.example.workforce.models.keys.NotificationId;

public interface NotificationRepository extends JpaRepository<Notification, NotificationId> {

  List<Notification> findByMemberId(Integer memberId); // may need conversion if your Member.id is Integer

  // get latest notif_seq for a member (0 if none)
  @Query("SELECT COALESCE(MAX(n.id.notifSeq), 0) FROM Notification n WHERE n.id.memberId = :memberId")
  Integer findMaxNotifSeqByMemberId(@Param("memberId") Integer memberId);

  List<Notification> findByMemberIdOrderByTimestampDesc(Integer memberId);
}

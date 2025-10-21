package com.example.workforce.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.workforce.dtos.CreateNotificationRequest;
import com.example.workforce.dtos.NotificationDto;
import com.example.workforce.mappers.NotificationMapper;
import com.example.workforce.models.Member;
import com.example.workforce.models.Notification;
import com.example.workforce.models.Role;
import com.example.workforce.models.Shift;
import com.example.workforce.models.ShiftAssignment;
import com.example.workforce.models.keys.NotificationId;
import com.example.workforce.repositories.MemberRepository;
import com.example.workforce.repositories.NotificationRepository;
import com.example.workforce.repositories.ShiftAssignmentRepository;
import com.example.workforce.repositories.ShiftRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {
  private final NotificationRepository notificationRepository;
  private final MemberRepository memberRepository;
  private final ShiftAssignmentRepository shiftAssignmentRepository;
  private final ShiftRepository shiftRepository;

  private final NotificationMapper notificationMapper;


  @Transactional
  public NotificationDto createNotification(CreateNotificationRequest req) {
    Member member = memberRepository.findById(req.getMemberId().intValue())
        .orElseThrow(() -> new IllegalArgumentException("Member not found: " + req.getMemberId()));

    Integer nextSeq = notificationRepository.findMaxNotifSeqByMemberId(req.getMemberId());
    nextSeq = nextSeq + 1;

    Notification n = new Notification();
    NotificationId id = new NotificationId(req.getMemberId(), nextSeq);
    n.setId(id);
    n.setMember(member);
    n.setTimestamp(LocalDateTime.now());
    n.setMessage(req.getMessage());
    n.setTitle(req.getTitle());
    n.setViewTime(null);

    Notification saved = notificationRepository.save(n);
    return notificationMapper.toDto(saved);
  }


  @Transactional
  public List<NotificationDto> getNotificationsForMember(Integer memberId) {
    List<Notification> list = notificationRepository.findByMemberIdOrderByTimestampDesc(memberId);
    return list.stream().map(notificationMapper::toDto).collect(Collectors.toList());
  }


  @Transactional
  public NotificationDto markAsViewed(Integer memberId, Integer notifSeq) {
    NotificationId id = new NotificationId(memberId, notifSeq);
    Notification n = notificationRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
    n.setViewTime(LocalDateTime.now());
    Notification saved = notificationRepository.save(n);
    return notificationMapper.toDto(saved);
    
  }

  /**
   * Create notifications for all members assigned to a shift.
   * Message is auto-generated using shift + role info. Returns list of created NotificationDto.
   */
  @Transactional
  public List<NotificationDto> notifyAssignmentsForShift(Integer shiftId) {
    List<ShiftAssignment> assignments =
        shiftAssignmentRepository.findByShiftId(shiftId);

    Shift shift = shiftRepository.findById(shiftId)
        .orElseThrow(() -> new IllegalArgumentException("Shift not found: " + shiftId));

    List<NotificationDto> created = new ArrayList<>();

    for (ShiftAssignment a : assignments) {
      Member member = a.getMember();
      Role role = a.getRole();
      String msg = buildAssignmentMessage(shift, role);
      CreateNotificationRequest req = new CreateNotificationRequest(
          member.getId(),
          "Shift Assigned: " + (shift.getTitle() != null ? shift.getTitle() : "Shift"),
          msg
      );
      created.add(createNotification(req));
    }
    return created;
  }

  private String buildAssignmentMessage(Shift shift, Role role) {
    String loc = shift.getLocation() != null ? shift.getLocation().getStreet() + ", " + shift.getLocation().getCity() : "the assigned location";
    return String.format("You have been assigned to role '%s' on %s from %s to %s at %s.",
        role.getName(),
        shift.getDay(),
        shift.getStartTime(),
        shift.getEndTime(),
        loc
    );
  }

}

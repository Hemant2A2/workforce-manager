package com.example.workforce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.workforce.dtos.CreateNotificationRequest;
import com.example.workforce.dtos.NotificationDto;
import com.example.workforce.services.NotificationService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
  private final NotificationService notificationService;

  @PostMapping
  public ResponseEntity<NotificationDto> create(@RequestBody CreateNotificationRequest req) {
    NotificationDto dto = notificationService.createNotification(req);
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/member/{memberId}")
  public ResponseEntity<List<NotificationDto>> listForMember(@PathVariable Integer memberId) {
    List<NotificationDto> list = notificationService.getNotificationsForMember(memberId);
    return ResponseEntity.ok(list);
  }

  @PostMapping("/member/{memberId}/{notifSeq}/view")
  public ResponseEntity<NotificationDto> markViewed(@PathVariable Integer memberId, @PathVariable Integer notifSeq) {
    NotificationDto dto = notificationService.markAsViewed(memberId, notifSeq);
    return ResponseEntity.ok(dto);
  }

  /**
   * Trigger notifications for all current assignments in a shift.
   * call once assignments get created.
   */
  @PostMapping("/shift/{shiftId}/notify-assignments")
  public ResponseEntity<List<NotificationDto>> notifyAssignments(@PathVariable Integer shiftId) {
    List<NotificationDto> created = notificationService.notifyAssignmentsForShift(shiftId);
    return ResponseEntity.ok(created);
  }

}

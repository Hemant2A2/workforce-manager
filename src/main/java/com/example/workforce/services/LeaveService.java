package com.example.workforce.services;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.workforce.dtos.ApproveLeaveRequest;
import com.example.workforce.dtos.CreateNotificationRequest;
import com.example.workforce.dtos.LeaveRequestDto;
import com.example.workforce.dtos.ShiftAssignmentDto;
import com.example.workforce.dtos.SubmitLeaveRequest;
import com.example.workforce.enums.Attendance;
import com.example.workforce.enums.LeaveApproval;
import com.example.workforce.models.LeaveRequest;
import com.example.workforce.models.Member;
import com.example.workforce.models.Requirement;
import com.example.workforce.models.Role;
import com.example.workforce.models.Shift;
import com.example.workforce.models.ShiftAssignment;
import com.example.workforce.models.Week;
import com.example.workforce.models.keys.LeaveRequestId;
import com.example.workforce.repositories.LeaveRequestRepository;
import com.example.workforce.repositories.MemberRepository;
import com.example.workforce.repositories.RequirementRepository;
import com.example.workforce.repositories.ShiftAssignmentRepository;
import com.example.workforce.repositories.ShiftRepository;
import com.example.workforce.repositories.WeekRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LeaveService {
  private final LeaveRequestRepository leaveRequestRepository;
  private final MemberRepository memberRepository;
  private final ShiftRepository shiftRepository;
  private final NotificationService notificationService;
  private final ShiftAssignmentRepository shiftAssignmentRepository;
  private final RequirementRepository requirementRepository;
  private final WeekRepository weekRepository;

  private final ShiftAssignmentService shiftAssignmentService;

  /**
   * Employee submits a leave request (PENDING).
   * Notifies the manager(s) of the shift location.
   */
  @Transactional
  public LeaveRequestDto submitLeaveRequest(SubmitLeaveRequest dto) {
    Member member = memberRepository.findById(dto.getMemberId())
        .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    Shift shift = shiftRepository.findById(dto.getShiftId())
        .orElseThrow(() -> new IllegalArgumentException("Shift not found"));

    LeaveRequestId id = new LeaveRequestId(member.getId(), shift.getId());
    LeaveRequest existing = leaveRequestRepository.findById(id).orElse(null);
    if (existing != null) {
      throw new IllegalArgumentException("Leave request already exists for this member & shift");
    }

    LeaveRequest lr = new LeaveRequest();
    lr.setId(id);
    lr.setMember(member);
    lr.setShift(shift);
    lr.setApproval(LeaveApproval.PENDING);
    leaveRequestRepository.save(lr);

    Member manager = shift.getLocation() != null ? shift.getLocation().getManager() : null;
    if (manager != null) {
      String title = "Leave request pending: " + member.getFName() + " " + member.getLName();
      String msg = String.format("%s requested leave for shift on %s (%s - %s).",
          member.getFName(), shift.getDay(), shift.getStartTime(), shift.getEndTime());
      notificationService.createNotification(new com.example.workforce.dtos.CreateNotificationRequest(
          Integer.valueOf(manager.getId()), title, msg));
    }

    LeaveRequestDto out = new LeaveRequestDto(
                            member.getId(), 
                            shift.getId(), 
                            "PENDING",
                            shift.getDay(), 
                            dto.getReason()
                          );
    return out;
  }

  /**
   * Manager approves / rejects a leave request
   * - if APPROVED: mark as APPROVED, add to member.unavailableShifts (so future auto-assign avoids),
   *   if member had an assignment for that shift/week mark their assignment attendance = LEAVE,
   *   then find replacements and auto-assign replacements (up to the requirement count freed).
   * - send notifications to member + replacements
   */
  @Transactional
  public List<ShiftAssignmentDto> handleApproveReject(ApproveLeaveRequest dto, Integer managerId) {
    LeaveRequestId id = new LeaveRequestId(dto.getMemberId(), dto.getShiftId());
    LeaveRequest lr = leaveRequestRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

    Member member = lr.getMember();
    Shift shift = lr.getShift();

    if (!dto.isApprove()) {
      lr.setApproval(LeaveApproval.REJECTED);
      leaveRequestRepository.save(lr);

      // notify member
      String title = "Leave request rejected";
      String msg = "Your leave request for shift on " + shift.getDay() + " was rejected.";
      notificationService.createNotification(new CreateNotificationRequest(
          Integer.valueOf(member.getId()), title, msg));
      return Collections.emptyList();
    }

    lr.setApproval(LeaveApproval.APPROVED);
    leaveRequestRepository.save(lr);

    member.getUnavailableShifts().add(shift);
    memberRepository.save(member);

    Week week = getWeekForShift(shift);

    // if member has an assignment for that shift+week -> mark attendance as LEAVE so slot opens
    Optional<ShiftAssignment> maybeAssign =
        shiftAssignmentRepository.findByShiftIdAndMemberIdAndWeekId(shift.getId(), member.getId(), week.getId());
    if (maybeAssign.isPresent()) {
      ShiftAssignment assignment = maybeAssign.get();
      assignment.setAttendance(Attendance.LEAVE);
      shiftAssignmentRepository.save(assignment);
    }

    List<Requirement> requirements = requirementRepository.findByShiftId(shift.getId());

    List<ShiftAssignmentDto> newAssignments = new ArrayList<>();
    for (Requirement req : requirements) {
      // count how many are assigned currently for this role in this shift-week
      long assignedCount = shiftAssignmentRepository.findByShiftId(shift.getId()).stream()
          .filter(a -> a.getRole().getId().equals(req.getRole().getId()))
          .filter(a -> a.getWeek() != null && Objects.equals(a.getWeek().getId(), week.getId()))
          .filter(a -> !Attendance.LEAVE.equals(a.getAttendance())) // only count active assignments
          .count();

      int needed = req.getCount() - (int) assignedCount;
      // needed > 0 -> fill using auto-assign logic
      for (int i = 0; i < needed; i++) {
        // find one candidate for req.role
        Optional<Member> replacement = findReplacementCandidateForRole(shift, req.getRole(), week);
        if (replacement.isPresent()) {
          ShiftAssignmentDto assigned = shiftAssignmentService.assignMemberToShift(shift, replacement.get(), req.getRole());
          newAssignments.add(assigned);
          // notify replacement
          String title = "Assigned to cover shift";
          String message = String.format("You have been assigned as %s on %s from %s to %s at %s",
              req.getRole().getName(), shift.getDay(), shift.getStartTime(), shift.getEndTime(),
              shift.getLocation() != null ? (shift.getLocation().getStreet() + ", " + shift.getLocation().getCity()) : "location");
          notificationService.createNotification(new CreateNotificationRequest(
              Integer.valueOf(replacement.get().getId()), title, message));
        } else {
          // no candidate found -> optionally notify manager that slot remains unfilled
          String managerMsg = "No replacement found for role " + req.getRole().getName() + " on shift " + shift.getDay();
          Member manager = shift.getLocation() != null ? shift.getLocation().getManager() : null;
          if (manager != null) {
            notificationService.createNotification(new CreateNotificationRequest(
                Integer.valueOf(manager.getId()), "Replacement needed", managerMsg));
          }
        }
      }
    }

    // notify the originally requesting member about approval
    notificationService.createNotification(new CreateNotificationRequest(
        Integer.valueOf(member.getId()), "Leave approved", "Your leave request for shift " + shift.getDay() + " has been approved."));

    return newAssignments;
  }

  /**
   * Unscheduled absence: manager marks a member absent for a shift immediately.
   * This will create the LeaveRequest (APPROVED) if not present, mark attendance, and trigger replacement logic.
   */
  @Transactional
  public List<ShiftAssignmentDto> markAbsent(Integer managerId, Integer memberId, Integer shiftId, String reason) {
    // create or update a LeaveRequest with APPROVED
    LeaveRequestId id = new LeaveRequestId(memberId, shiftId);
    LeaveRequest lr = leaveRequestRepository.findById(id).orElse(null);
    if (lr == null) {
      lr = new LeaveRequest();
      lr.setId(id);
      lr.setMember(memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found")));
      lr.setShift(shiftRepository.findById(shiftId).orElseThrow(() -> new IllegalArgumentException("Shift not found")));
    }
    lr.setApproval(LeaveApproval.APPROVED);
    leaveRequestRepository.save(lr);

    // delegate to approve flow to mark assignment and find replacements
    ApproveLeaveRequest dto = new ApproveLeaveRequest(memberId, shiftId, true, reason);
    return handleApproveReject(dto, managerId);
  }

  public List<LeaveRequestDto> getPendingForManager(Integer managerId) {
    return leaveRequestRepository.findAll().stream()
        .filter(lr -> lr.getApproval() == LeaveApproval.PENDING)
        .filter(lr -> lr.getShift() != null
            && lr.getShift().getLocation() != null
            && lr.getShift().getLocation().getManager() != null
            && Objects.equals(lr.getShift().getLocation().getManager().getId(), managerId))
        .map(lr -> new LeaveRequestDto(
            lr.getMember().getId(),
            lr.getShift().getId(),
            lr.getApproval().name(),
            lr.getShift().getDay(),
            null // reason not persisted in model; return null
        ))
        .toList();
  }

  /**
   * Finds one replacement candidate for the role and shift/week.
   * Ranking: (1) candidates who prefer overtime (overtimeRequired>0) first,
   * then members with fewer assigned hours this week.
   */
  private Optional<Member> findReplacementCandidateForRole(Shift shift, Role role, Week week) {
    // fetch all members who can perform this role
    List<Member> candidates = memberRepository.findAll().stream()
        .filter(m -> m.getFeasibleRoles().contains(role))
        .filter(m -> isAvailableForShift(m, shift))
        .collect(Collectors.toList());

    // remove those already assigned for this shift-week
    Set<Integer> alreadyAssigned = shiftAssignmentRepository.findByShiftId(shift.getId()).stream()
        .filter(a -> a.getWeek() != null && Objects.equals(a.getWeek().getId(), week.getId()))
        .map(a -> a.getMember().getId())
        .collect(Collectors.toSet());

    candidates = candidates.stream()
        .filter(m -> !alreadyAssigned.contains(m.getId()))
        .collect(Collectors.toList());

    if (candidates.isEmpty()) return Optional.empty();

    // compute assigned hours for each candidate this week (approx by summing shift durations of their assignments)
    Map<Integer, Double> assignedHours = new HashMap<>();
    for (Member c : candidates) {
      double hours = shiftAssignmentRepository.findByMemberId(c.getId()).stream()
        .filter(a -> a.getWeek() != null && Objects.equals(a.getWeek().getId(), week.getId()))
        .mapToDouble(a -> {
          Shift s = a.getShift();
          if (s == null) return 0.0;
          long secs = java.time.Duration.between(s.getStartTime(), s.getEndTime()).getSeconds();
          return secs / 3600.0;
        })
        .sum();
      assignedHours.put(c.getId(), hours);
    }

    // sort candidates: prefer those with overtimeRequired > 0, then ascending assigned hours
    candidates.sort(Comparator.comparing((Member m) -> m.getOvertimeRequired() <= 0) // false (overtime>0) first
        .thenComparing(m -> assignedHours.getOrDefault(m.getId(), 0.0)));

    return Optional.of(candidates.get(0));
  }

  // helper: same logic as earlier service to get or create week from shift day
  private Week getWeekForShift(Shift shift) {
    try {
      LocalDate date = LocalDate.parse(shift.getDay());
      LocalDate monday = date.minusDays((date.getDayOfWeek().getValue() - 1));
      return weekRepository.findByStartDate(monday)
          .orElseGet(() -> {
            Week w = new Week();
            w.setStartDate(monday);
            return weekRepository.save(w);
          });
    } catch (DateTimeParseException ex) {
      throw new IllegalStateException("Shift day must be ISO date yyyy-MM-dd to compute week");
    }
  }

  // reuse availability check from ShiftService (or copy here)
  private boolean isAvailableForShift(Member member, Shift shift) {
    if (member.getUnavailableShifts() == null) return true;
    return member.getUnavailableShifts().stream().noneMatch(us -> {
      if (!shift.getDay().equals(us.getDay())) return false;
      boolean overlap = !(shift.getEndTime().isBefore(us.getStartTime()) || shift.getStartTime().isAfter(us.getEndTime()));
      return overlap;
    });
  }

}

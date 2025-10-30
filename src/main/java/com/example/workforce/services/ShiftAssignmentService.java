package com.example.workforce.services;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.workforce.dtos.ShiftAssignmentDto;
import com.example.workforce.enums.Attendance;
import com.example.workforce.models.Member;
import com.example.workforce.models.Requirement;
import com.example.workforce.models.Role;
import com.example.workforce.models.Shift;
import com.example.workforce.models.ShiftAssignment;
import com.example.workforce.models.Week;
import com.example.workforce.models.keys.ShiftAssignmentId;
import com.example.workforce.repositories.MemberRepository;
import com.example.workforce.repositories.RequirementRepository;
import com.example.workforce.repositories.RoleRepository;
import com.example.workforce.repositories.ShiftAssignmentRepository;
import com.example.workforce.repositories.ShiftRepository;
import com.example.workforce.repositories.WeekRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ShiftAssignmentService {
  private final ShiftRepository shiftRepository;
  private final RequirementRepository requirementRepository;
  private final RoleRepository roleRepository;
  private final MemberRepository memberRepository;
  private final ShiftAssignmentRepository shiftAssignmentRepository;
  private final WeekRepository weekRepository;

  @Transactional
  public List<ShiftAssignmentDto> autoAssignMembers(Integer shiftId) {
    Shift shift = shiftRepository.findById(shiftId)
                  .orElseThrow(() -> new IllegalArgumentException("Shift not found"));

    Week week = getOrCreateWeekForShift(shift);
    Integer weekId = week.getId();

    List<ShiftAssignment> existingAssignments = shiftAssignmentRepository.findByShiftId(shiftId);
    Set<Integer> alreadyAssignedMemberIds = existingAssignments.stream()
      .filter(a -> a.getWeek() != null && Objects.equals(a.getWeek().getId(), weekId))
      .map(a -> a.getMember().getId())
      .collect(Collectors.toSet());

    List<Requirement> requirements = requirementRepository.findAll()
                                    .stream()
                                    .filter(r -> r.getShift().getId().equals(shiftId))
                                    .collect(Collectors.toList());

    List<ShiftAssignmentDto> shiftAssignments = new ArrayList<>();
    Set<Integer> assignedThisRun = new HashSet<>();

    for(Requirement requirement: requirements) {
      Role role = requirement.getRole();
      List<Member> candidates = memberRepository.findAll().stream()
                                .filter(m -> m.getFeasibleRoles().contains(role))
                                .filter(m -> isAvailableForShift(m, shift))
                                .filter(m -> !alreadyAssignedMemberIds.contains(m.getId()))
                                .filter(m -> !assignedThisRun.contains(m.getId()))
                                .collect(Collectors.toList());

      // TODO:  sort candidates by some fairness criteria (least assigned recently, hours worked, etc)

      int need = requirement.getCount();
      for (Member candidate : candidates) {
        if (need <= 0) break;
        ShiftAssignmentDto dto = assignMemberToShift(shift, candidate, role);
        shiftAssignments.add(dto);
        assignedThisRun.add(candidate.getId());
        alreadyAssignedMemberIds.add(candidate.getId());
        need--;
      }
    }
    return shiftAssignments;
  }


  @Transactional
  public ShiftAssignmentDto manualAssignMember(Integer shiftId, Integer memberId, Integer roleId) {
    Shift shift = shiftRepository.findById(shiftId)
        .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    Role role = roleRepository.findById(roleId)
        .orElseThrow(() -> new IllegalArgumentException("Role not found"));

    if (!member.getFeasibleRoles().contains(role)) {
      throw new IllegalArgumentException("Member not feasible for role");
    }
    if (!isAvailableForShift(member, shift)) {
      throw new IllegalArgumentException("Member not available for shift");
    }

    return assignMemberToShift(shift, member, role);
  }

  @Transactional
  public ShiftAssignmentDto assignMemberToShift(Shift shift, Member member, Role role) {
    Week week = getOrCreateWeekForShift(shift);
    Integer weekId = week.getId();

    List<ShiftAssignment> existingForShift = shiftAssignmentRepository.findByShiftId(shift.getId());
    boolean alreadyAssigned = existingForShift.stream()
                              .anyMatch(a -> a.getWeek() != null && Objects.equals(a.getWeek().getId(), weekId)
                              && Objects.equals(a.getMember().getId(), member.getId()));
    if (alreadyAssigned) {
      throw new IllegalArgumentException("Member " + member.getId() + " is already assigned for this shift/week");
    }

    ShiftAssignmentId id = new ShiftAssignmentId(
      shift.getId(), 
      member.getId(), 
      weekId, 
      role.getId()
    );

    ShiftAssignment shiftAssignment = new ShiftAssignment();
    shiftAssignment.setId(id);
    shiftAssignment.setShift(shift);
    shiftAssignment.setWeek(week);
    shiftAssignment.setMember(member);
    shiftAssignment.setRole(role);
    shiftAssignment.setAttendance(Attendance.SCHEDULED);

    shiftAssignmentRepository.save(shiftAssignment);

    ShiftAssignmentDto dto = new ShiftAssignmentDto(
      shift.getId(),
      member.getId(),
      role.getId(),
      shiftAssignment.getAttendance().name()
    );

    return dto;
  }

  public List<ShiftAssignmentDto> getAssignmentsForShift(Integer shiftId) {
    List<ShiftAssignment> assignments = shiftAssignmentRepository.findByShiftId(shiftId);
    return assignments.stream()
        .map(a -> new ShiftAssignmentDto(
            a.getShift().getId(),
            a.getMember().getId(),
            a.getRole().getId(),
            a.getAttendance() != null ? a.getAttendance().name() : null
        ))
        .toList();
  }

  @Transactional
  public ShiftAssignmentDto markAttendance(Integer shiftId, Integer memberId, String status, Integer managerId) {
    Shift shift = shiftRepository.findById(shiftId)
        .orElseThrow(() -> new IllegalArgumentException("Shift not found"));

    // Validate manager permissions: manager must belong to shift's location and be a MANAGER
    Member manager = memberRepository.findById(managerId)
        .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
    if (manager.getMemberType() == null || manager.getMemberType().getTitle() == null
        || manager.getWorksAt() == null
        || !manager.getWorksAt().getId().equals(shift.getLocation().getId())) {
      throw new IllegalArgumentException("Not authorized to mark attendance for this shift");
    }

    // Check ongoing: today's date equals shift day and current time within window
    LocalDate today;
    try {
      today = LocalDate.parse(shift.getDay());
    } catch (DateTimeParseException ex) {
      throw new IllegalStateException("Shift day must be ISO date yyyy-MM-dd");
    }
    var nowDate = LocalDate.now();
    var nowTime = java.time.LocalTime.now();
    if (!nowDate.equals(today) || nowTime.isBefore(shift.getStartTime()) || nowTime.isAfter(shift.getEndTime())) {
      throw new IllegalArgumentException("Attendance can only be marked while the shift is ongoing");
    }

    // Resolve current week for the shift day
    Week week = getOrCreateWeekForShift(shift);
    Integer weekId = week.getId();

    // Find assignment and update attendance
    ShiftAssignment assignment = shiftAssignmentRepository
        .findByShiftIdAndMemberIdAndWeekId(shiftId, memberId, weekId)
        .orElseThrow(() -> new IllegalArgumentException("Member is not assigned to this shift/week"));

    Attendance att;
    try {
      att = Attendance.valueOf(status.toUpperCase());
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid attendance status. Use PRESENT or ABSENT");
    }
    if (att == Attendance.LEAVE) {
      throw new IllegalArgumentException("LEAVE is managed via leave workflow");
    }
    assignment.setAttendance(att);
    shiftAssignmentRepository.save(assignment);

    return new ShiftAssignmentDto(
      shiftId,
      memberId,
      assignment.getRole().getId(),
      assignment.getAttendance().name()
    );
  }



  private Week getOrCreateWeekForShift(Shift shift) {
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

  private boolean isAvailableForShift(Member member, Shift shift) {
    if (member.getUnavailableShifts() == null) return true;
    return member.getUnavailableShifts().stream().noneMatch(us -> {
      if (!shift.getDay().equals(us.getDay())) return false;
      // overlapping time?
      boolean overlap = ! (shift.getEndTime().isBefore(us.getStartTime()) || shift.getStartTime().isAfter(us.getEndTime()));
      return overlap;
    });
  }

}

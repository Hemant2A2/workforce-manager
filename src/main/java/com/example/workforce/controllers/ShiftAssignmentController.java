package com.example.workforce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.workforce.dtos.AssignMembersRequest;
import com.example.workforce.dtos.MarkAttendanceRequest;
import com.example.workforce.dtos.ShiftAssignmentDto;
import com.example.workforce.services.AuthService;
import com.example.workforce.services.ShiftAssignmentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/shift_assignments")
public class ShiftAssignmentController {
  private final ShiftAssignmentService shiftAssignmentService;
  private final AuthService authService;

  @PostMapping("/{id}/auto")
  public ResponseEntity<List<ShiftAssignmentDto>> autoAssignMembers(@PathVariable Integer id) {
    return ResponseEntity.ok(shiftAssignmentService.autoAssignMembers(id));
  }

  @PostMapping("/{id}/manual")
  public ResponseEntity<ShiftAssignmentDto> manualAssignMember(@PathVariable Integer id, @RequestBody AssignMembersRequest.ManualAssignment manual) {
    ShiftAssignmentDto dto = shiftAssignmentService.manualAssignMember(id, manual.getMemberId(), manual.getRoleId());
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<ShiftAssignmentDto>> getAssignmentsForShift(@PathVariable Integer id) {
    List<ShiftAssignmentDto> items = shiftAssignmentService.getAssignmentsForShift(id);
    return ResponseEntity.ok(items);
  }

  // Manager marks attendance for a member during an ongoing shift
  @PostMapping("/{id}/attendance")
  public ResponseEntity<ShiftAssignmentDto> markAttendance(
      @PathVariable Integer id,
      @RequestBody MarkAttendanceRequest request) {
    var manager = authService.getCurrentUser();
    if (manager == null) {
      return ResponseEntity.status(401).build();
    }
    ShiftAssignmentDto dto = shiftAssignmentService.markAttendance(
        id,
        request.getMemberId(),
        request.getStatus(),
        manager.getId()
    );
    return ResponseEntity.ok(dto);
  }

}

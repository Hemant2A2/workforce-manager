package com.example.workforce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.workforce.dtos.AssignMembersRequest;
import com.example.workforce.dtos.ShiftAssignmentDto;
import com.example.workforce.services.ShiftAssignmentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/shift_assignments")
public class ShiftAssignmentController {
  private final ShiftAssignmentService shiftAssignmentService;

  @PostMapping("/{id}/auto")
  public ResponseEntity<List<ShiftAssignmentDto>> autoAssignMembers(@PathVariable Integer id) {
    return ResponseEntity.ok(shiftAssignmentService.autoAssignMembers(id));
  }

  @PostMapping("/{id}/manual")
  public ResponseEntity<ShiftAssignmentDto> manualAssignMember(@PathVariable Integer id, @RequestBody AssignMembersRequest.ManualAssignment manual) {
    ShiftAssignmentDto dto = shiftAssignmentService.manualAssignMember(id, manual.getMemberId(), manual.getRoleId());
    return ResponseEntity.ok(dto);
  }

}

// package com.example.workforce.controllers;

// import java.util.Collections;
// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.workforce.dtos.ApproveLeaveRequest;
// import com.example.workforce.dtos.LeaveRequestDto;
// import com.example.workforce.dtos.ShiftAssignmentDto;
// import com.example.workforce.dtos.SubmitLeaveRequest;
// import com.example.workforce.services.LeaveService;

// import lombok.AllArgsConstructor;

// @RestController
// @AllArgsConstructor
// @RequestMapping("/leaves")
// public class LeaveController {
//   private final LeaveService leaveService;

//   // employee submits leave request
//   @PostMapping("/request")
//   public ResponseEntity<LeaveRequestDto> submit(@RequestBody SubmitLeaveRequest dto) {
//     LeaveRequestDto out = leaveService.submitLeaveRequest(dto);
//     return ResponseEntity.status(201).body(out);
//   }

//   // manager approves / rejects
//   @PostMapping("/handle")
//   public ResponseEntity<List<ShiftAssignmentDto>> handle(
//     @RequestBody ApproveLeaveRequest dto, 
//     @RequestHeader("X-User-Id") Integer managerId) {
//     // X-User-Id simulates authenticated manager id; integrate with your security layer
//     List<ShiftAssignmentDto> assigned = leaveService.handleApproveReject(dto, managerId);
//     return ResponseEntity.ok(assigned);
//   }

//   // manager marks absent (unscheduled)
//   @PostMapping("/absent")
//   public ResponseEntity<List<ShiftAssignmentDto>> markAbsent(
//       @RequestParam Integer managerId,
//       @RequestParam Integer memberId,
//       @RequestParam Integer shiftId,
//       @RequestParam(required = false) String reason) {
//     List<ShiftAssignmentDto> assigned = leaveService.markAbsent(managerId, memberId, shiftId, reason);
//     return ResponseEntity.ok(assigned);
//   }

//   // list pending requests for manager's locations
//   @GetMapping("/pending/manager/{managerId}")
//   public ResponseEntity<List<LeaveRequestDto>> pendingForManager(@PathVariable Integer managerId) {
//     // you can implement a repo query to fetch pending leave requests for shifts where location.manager == managerId
//     // for simplicity, leave service can expose such method - not included here for brevity
//     return ResponseEntity.ok(Collections.emptyList()); // replace with real implementation
//   }

// }

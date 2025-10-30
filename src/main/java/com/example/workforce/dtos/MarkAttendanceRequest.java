package com.example.workforce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkAttendanceRequest {
  private Integer memberId;
  // ACCEPTED: PRESENT or ABSENT
  private String status;
}

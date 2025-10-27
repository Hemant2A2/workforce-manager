package com.example.workforce.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ApproveLeaveRequest {
  private Integer memberId;
  private Integer shiftId;
  private boolean approve; // true => APPROVE, false => REJECT
  private String reason;
}

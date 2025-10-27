package com.example.workforce.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LeaveRequestDto {
  private Integer memberId;
  private Integer shiftId;
  private String approval; // PENDING|APPROVED|REJECTED
  private String shiftDay;
  private String reason;
}

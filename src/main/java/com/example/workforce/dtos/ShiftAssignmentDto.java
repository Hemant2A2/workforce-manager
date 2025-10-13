package com.example.workforce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ShiftAssignmentDto {
  private Integer shiftId;
  private Integer memberId;
  private Integer roleId;
  private String attendance;
}

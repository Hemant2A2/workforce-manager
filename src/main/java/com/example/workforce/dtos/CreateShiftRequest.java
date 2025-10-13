package com.example.workforce.dtos;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CreateShiftRequest {
  private String title;
  private String day; // format yyy-mm-dd
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer locationId;
  private List<RequirementDto> requirements;
}

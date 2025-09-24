package com.example.workforce.dtos;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CreateShiftRequest {

  private String title;
  private String day;
  private LocalTime startTime;
  private LocalTime endTime;

}

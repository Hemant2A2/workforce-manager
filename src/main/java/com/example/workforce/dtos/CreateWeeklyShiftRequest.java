package com.example.workforce.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateWeeklyShiftRequest {
  private LocalDate weekStartDate;
  private List<CreateShiftRequest> perDayShifts;
}

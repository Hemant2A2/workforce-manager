package com.example.workforce.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateNotificationRequest {
  private Integer memberId;   // recipient member id
  private String title;
  private String message;
}

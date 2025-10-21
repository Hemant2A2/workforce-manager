package com.example.workforce.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationDto {
  private Long memberId;
  private Long notifSeq;
  private LocalDateTime timestamp;
  private LocalDateTime viewTime;
  private String title;
  private String message;
}

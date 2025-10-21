package com.example.workforce.mappers;

import org.mapstruct.*;
import com.example.workforce.models.Notification;
import com.example.workforce.dtos.NotificationDto;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(target = "memberId", source = "member.id")
  @Mapping(target = "notifSeq", source = "id.notifSeq")
  NotificationDto toDto(Notification n);
}

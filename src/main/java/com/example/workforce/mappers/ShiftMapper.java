package com.example.workforce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.example.workforce.dtos.CreateShiftRequest;
import com.example.workforce.dtos.ShiftDto;
import com.example.workforce.models.Shift;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
  ShiftDto toDto(Shift shift);
  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "location", ignore = true),
    @Mapping(target = "unavailableMembers", ignore = true),
  })
  Shift toEntity(CreateShiftRequest request);
}

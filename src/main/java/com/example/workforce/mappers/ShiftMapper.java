package com.example.workforce.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.workforce.dtos.RequirementDto;
import com.example.workforce.dtos.ShiftDto;
import com.example.workforce.models.Shift;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
  @Mapping(target = "locationId", source = "location.id")
  @Mapping(target = "requirements", expression = "java(mapRequirements(shift))")
  ShiftDto toDto(Shift shift);

  default List<RequirementDto> mapRequirements(Shift shift) {
    if (shift == null || shift.getId() == null) return java.util.Collections.emptyList();
    if (shift.getRequirements() == null) return java.util.Collections.emptyList();
    return shift.getRequirements().stream()
        .map(r -> new RequirementDto(r.getRole().getId(), r.getCount()))
        .collect(Collectors.toList());
  }

  // @Mappings({
  //   @Mapping(target = "id", ignore = true),
  //   @Mapping(target = "location", ignore = true),
  //   @Mapping(target = "unavailableMembers", ignore = true),
  // })
  // Shift toEntity(CreateShiftRequest request);
}

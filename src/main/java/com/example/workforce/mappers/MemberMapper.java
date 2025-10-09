package com.example.workforce.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.example.workforce.dtos.MemberDto;
import com.example.workforce.dtos.RegisterMemberRequest;
import com.example.workforce.dtos.UpdateMemberRequest;
import com.example.workforce.models.Member;


@Mapper(componentModel = "spring")
public interface MemberMapper {
  @Mappings({
    @Mapping(target = "worksAt", source = "worksAt.id"),
    @Mapping(target = "type", source = "memberType.title")
  })
  MemberDto toDto(Member member);
  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "apartment", ignore = true),
    @Mapping(target = "city", ignore = true),
    @Mapping(target = "dob", ignore = true),
    @Mapping(target = "overtimeRequired", ignore = true),
    @Mapping(target = "availedLeaves", ignore = true),
    @Mapping(target = "feasibleRoles", ignore = true),
    @Mapping(target = "unavailableShifts", ignore = true),
    @Mapping(target = "worksAt", ignore = true),
    @Mapping(target = "memberType", ignore = true)
  })
  Member toEntity(RegisterMemberRequest request);
  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "gender", ignore = true),
    @Mapping(target = "password", ignore = true),
    @Mapping(target = "dob", ignore = true),
    @Mapping(target = "overtimeRequired", ignore = true),
    @Mapping(target = "availedLeaves", ignore = true),
    @Mapping(target = "feasibleRoles", ignore = true),
    @Mapping(target = "unavailableShifts", ignore = true),
    @Mapping(target = "worksAt", ignore = true),
    @Mapping(target = "memberType", ignore = true)
  })
  void update(UpdateMemberRequest request, @MappingTarget Member member);

}

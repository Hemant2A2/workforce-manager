package com.example.workforce.dtos;

import java.time.LocalDate;
import java.util.List;

import com.example.workforce.enums.Gender;
import com.example.workforce.enums.Title;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class MemberDto {
  private Integer id;
  private String fName;
  private String mName;
  private String lName;
  private Title type;
  private List<Integer> feasibleRoles;
  private Integer worksAt;
  private Gender gender;
  private double overtimeRequired;
  private Integer availedLeaves;
  private LocalDate dob;
  private String phone;
  private String apartment;
  private String city;
}

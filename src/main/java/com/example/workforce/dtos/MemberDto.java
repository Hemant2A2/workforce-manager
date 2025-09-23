package com.example.workforce.dtos;

import java.time.LocalDate;

import com.example.workforce.enums.Gender;

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
  private Gender gender;
  private LocalDate dob;
  private String phone;
  private String apartment;
  private String city;
}

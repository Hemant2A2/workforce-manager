package com.example.workforce.dtos;

import com.example.workforce.enums.Gender;
import com.example.workforce.enums.Title;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class RegisterMemberRequest {
  private String fName;
  private String mName;
  private String lName;
  private Title type;
  private Integer worksAt;
  private Integer allowedPaidLeaves;
  private Integer allowedHours;
  private Gender gender;
  private String phone;
  private String password;
}

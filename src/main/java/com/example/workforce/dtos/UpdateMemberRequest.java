package com.example.workforce.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class UpdateMemberRequest {

  private String fName;
  private String mName;
  private String lName;
  private String apartment;
  private String city;
  private String phone;
}

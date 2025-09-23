package com.example.workforce.dtos;

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
  private String phone;
  private String password;
}

package com.example.workforce.dtos;

import lombok.Data;

@Data
public class LoginRequest {
  private Integer id;
  private String password;
}

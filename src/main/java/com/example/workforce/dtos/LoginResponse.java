package com.example.workforce.dtos;

import com.example.workforce.config.Jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
  private Jwt accessToken;
  private Jwt refreshToken;
}

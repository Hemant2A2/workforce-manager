package com.example.workforce.config;

import java.util.Date;

import javax.crypto.SecretKey;

import com.example.workforce.enums.Title;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Jwt {
  private final Claims claims;
  private final SecretKey secretKey;

  public Jwt(Claims claims, SecretKey secretKey) {
    this.claims = claims;
    this.secretKey = secretKey;
  }

  public boolean isExpired() {
    return claims.getExpiration().before(new Date());
  }

  public Integer getUserId() {
    return Integer.valueOf(claims.getSubject());
  }

  public Title getType() {
    return Title.valueOf(claims.get("type", String.class));
  }

  public String toString() {
    return Jwts.builder().claims(claims).signWith(secretKey).compact();
  }
}

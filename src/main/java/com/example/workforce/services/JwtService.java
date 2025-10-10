package com.example.workforce.services;

import java.util.Date;
import org.springframework.stereotype.Service;

import com.example.workforce.config.Jwt;
import com.example.workforce.config.JwtConfig;
import com.example.workforce.models.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class JwtService {
  private final JwtConfig jwtConfig;

  public Jwt generateAccessToken(Member user) {
    return generateToken(user, jwtConfig.getAccessTokenExpiration());
  }

  public Jwt generateRefreshToken(Member user) {
    return generateToken(user, jwtConfig.getRefreshTokenExpiration());
  }

  private Jwt generateToken(Member user, long tokenExpiration) {
    var claims = Jwts.claims()
            .subject(user.getId().toString())
            .add("name", user.getFName() + " " + user.getMName() + " " + user.getLName())
            .add("worksAt", user.getWorksAt().getId())
            .add("type", user.getMemberType().getTitle().name())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
            .build();

    return new Jwt(claims, jwtConfig.getSecretKey());
  }

  public Jwt parseToken(String token) {
    try {
      var claims = getClaims(token);
      return new Jwt(claims, jwtConfig.getSecretKey());
    } catch (JwtException e) {
      return null;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
            .verifyWith(jwtConfig.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }
}

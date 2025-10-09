package com.example.workforce.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.workforce.config.Jwt;
import com.example.workforce.dtos.LoginRequest;
import com.example.workforce.dtos.LoginResponse;
import com.example.workforce.models.Member;
import com.example.workforce.repositories.MemberRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final MemberRepository memberRepository;
  private final JwtService jwtService;

  public Member getCurrentUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var memberId = (Integer) authentication.getPrincipal();

    return memberRepository.findById(memberId).orElse(null);
  }

  public LoginResponse login(LoginRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
          request.getId(),
          request.getPassword()
      )
    );

    var user = memberRepository.findById(request.getId()).orElseThrow();
    var accessToken = jwtService.generateAccessToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    return new LoginResponse(accessToken, refreshToken);
  }

  public Jwt refreshAccessToken(String refreshToken) {
    var jwt = jwtService.parseToken(refreshToken);
    if (jwt == null || jwt.isExpired()) {
        throw new BadCredentialsException("Invalid refresh token");
    }

    var user = memberRepository.findById(jwt.getUserId()).orElseThrow();
    return jwtService.generateAccessToken(user);
  }
}

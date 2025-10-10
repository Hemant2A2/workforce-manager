package com.example.workforce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;

import com.example.workforce.config.JwtConfig;
import com.example.workforce.dtos.JwtResponse;
import com.example.workforce.dtos.LoginRequest;
import com.example.workforce.dtos.MemberDto;
import com.example.workforce.mappers.MemberMapper;
import com.example.workforce.services.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
  private final JwtConfig jwtConfig;
    private final MemberMapper memberMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse login(
    @Valid @RequestBody LoginRequest request,
    HttpServletResponse response) {

    var loginResult = authService.login(request);

    var refreshToken = loginResult.getRefreshToken().toString();
    var cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/auth/refresh");
    cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
    cookie.setSecure(true);
    response.addCookie(cookie);

    return new JwtResponse(loginResult.getAccessToken().toString());
  }

  @PostMapping("/refresh")
  public JwtResponse refresh(@CookieValue(value = "refreshToken") String refreshToken) {
    var accessToken = authService.refreshAccessToken(refreshToken);
    return new JwtResponse(accessToken.toString());
  }

  @GetMapping("/me")
  public ResponseEntity<MemberDto> me() {
    var user = authService.getCurrentUser();
    if (user == null) {
        return ResponseEntity.notFound().build();
    }

    var memberDto = memberMapper.toDto(user);
    return ResponseEntity.ok(memberDto);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Void> handleBadCredentialsException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}

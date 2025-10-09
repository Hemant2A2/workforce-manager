package com.example.workforce.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import com.example.workforce.commons.SecurityRules;

@Component
public class AuthSecurityRules implements SecurityRules {
  @Override
  public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
    registry
        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll();
  }
}

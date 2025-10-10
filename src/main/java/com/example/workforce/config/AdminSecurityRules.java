package com.example.workforce.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import com.example.workforce.commons.SecurityRules;
import com.example.workforce.enums.Title;

@Component
public class AdminSecurityRules implements SecurityRules {
  @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
              .requestMatchers("/admin/**").hasRole(Title.ADMIN.name());
    }
}

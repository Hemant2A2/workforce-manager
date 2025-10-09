package com.example.workforce.config;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.workforce.models.Member;
import com.example.workforce.repositories.MemberRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserDetailsServiveImpl implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    var user = memberRepository.findById(Integer.valueOf(id)).orElseThrow(
            () -> new UsernameNotFoundException("User not found"));

    return new User(
        String.valueOf(user.getId()),
        user.getPassword(),
        Collections.emptyList()
    );
  }
}

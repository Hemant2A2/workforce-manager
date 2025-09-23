package com.example.workforce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.workforce.dtos.MemberDto;
import com.example.workforce.dtos.RegisterMemberRequest;
import com.example.workforce.dtos.UpdateMemberRequest;
import com.example.workforce.services.MemberService;
import com.example.workforce.utils.MemberNotFoundException;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  @GetMapping
  public Iterable<MemberDto> getAllMembers() {
    return memberService.getAllMembers();
  }

  @GetMapping("/{id}")
  public MemberDto getMember(@PathVariable Integer id) {
    return memberService.getMember(id);
  }

  @PostMapping
  public ResponseEntity<?> registerMember(
    @RequestBody RegisterMemberRequest request,
    UriComponentsBuilder uriBuilder) {
      
      var memberDto = memberService.registerMember(request);
      var uri = uriBuilder.path("/members/{id}").buildAndExpand(memberDto.getId()).toUri();
      return ResponseEntity.created(uri).body(memberDto);
  }

  @PutMapping("/{id}")
  public MemberDto updateMember(
    @PathVariable Integer id,
    @RequestBody UpdateMemberRequest request) {
    return memberService.updateMember(id, request);
  }

  @DeleteMapping("/{id}")
  public void deleteMember(@PathVariable Integer id) {
    memberService.deleteMember(id);
  }

  @ExceptionHandler(MemberNotFoundException.class)
  public ResponseEntity<Void> handleMemberNotFound() {
      return ResponseEntity.notFound().build();
  }

  
}

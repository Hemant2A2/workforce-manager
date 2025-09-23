package com.example.workforce.services;

import org.springframework.stereotype.Service;

import com.example.workforce.dtos.MemberDto;
import com.example.workforce.mappers.MemberMapper;
import com.example.workforce.dtos.RegisterMemberRequest;
import com.example.workforce.dtos.UpdateMemberRequest;
import com.example.workforce.repositories.MemberRepository;
import com.example.workforce.utils.MemberNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberMapper memberMapper;

  public Iterable<MemberDto> getAllMembers() {
    return memberRepository.findAll()
           .stream()
           .map(memberMapper::toDto)
           .toList();
  }

  public MemberDto getMember(Integer id) {
    var member = memberRepository.findById(id)
                  .orElseThrow(MemberNotFoundException::new);
    return memberMapper.toDto(member);
  }

  public MemberDto registerMember(RegisterMemberRequest request) {
    var member = memberMapper.toEntity(request);
    member.setPassword(member.getPassword());
    memberRepository.save(member);
    return memberMapper.toDto(member);
  }

  public MemberDto updateMember(Integer id, UpdateMemberRequest request) {
    var member = memberRepository.findById(id)
                  .orElseThrow(MemberNotFoundException::new);
    memberMapper.update(request, member);
    memberRepository.save(member);
    return memberMapper.toDto(member);
  }

  public void deleteMember(Integer id) {
    var member = memberRepository.findById(id)
                  .orElseThrow(MemberNotFoundException::new);
    memberRepository.delete(member);
  }

}

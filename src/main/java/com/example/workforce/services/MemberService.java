package com.example.workforce.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.workforce.dtos.MemberDto;
import com.example.workforce.mappers.MemberMapper;
import com.example.workforce.models.Location;
import com.example.workforce.models.MemberType;
import com.example.workforce.dtos.RegisterMemberRequest;
import com.example.workforce.dtos.UpdateMemberRequest;
import com.example.workforce.repositories.LocationRepository;
import com.example.workforce.repositories.MemberRepository;
import com.example.workforce.repositories.MemberTypeRepository;
import com.example.workforce.utils.MemberNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MemberService {
  private final MemberRepository memberRepository;
  private final MemberTypeRepository memberTypeRepository;
  private final LocationRepository locationRepository;
  private final MemberMapper memberMapper;
  private final PasswordEncoder passwordEncoder;

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

  @Transactional
  public MemberDto registerMember(RegisterMemberRequest request) {
    var member = memberMapper.toEntity(request);
    member.setPassword(passwordEncoder.encode(member.getPassword()));
    MemberType memberType = new MemberType();
    memberType.setTitle(request.getType());
    memberType.setAllowedHours(request.getAllowedHours());
    memberType.setAllowedPaidLeaves(request.getAllowedPaidLeaves());
    memberTypeRepository.save(memberType);
    member.setMemberType(memberType);
    if(request.getWorksAt() != null) {
      Location location = locationRepository.findById(request.getWorksAt()).orElseThrow(() -> new EntityNotFoundException(
            "Location with id " + request.getWorksAt() + " not found"));
      member.setWorksAt(location);
      location.getMembers().add(member);
    }
    var saved = memberRepository.save(member);
    return memberMapper.toDto(saved);
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

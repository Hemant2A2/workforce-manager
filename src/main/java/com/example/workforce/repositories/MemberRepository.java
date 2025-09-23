package com.example.workforce.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workforce.models.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

}

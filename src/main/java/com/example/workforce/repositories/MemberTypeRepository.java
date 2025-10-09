package com.example.workforce.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workforce.enums.Title;
import com.example.workforce.models.MemberType;

public interface MemberTypeRepository extends JpaRepository<MemberType, Integer> {
  Optional<MemberType> findByTitle(Title title);
}

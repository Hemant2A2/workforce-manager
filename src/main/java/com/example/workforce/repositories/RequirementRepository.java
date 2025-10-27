package com.example.workforce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workforce.models.Requirement;
import com.example.workforce.models.keys.RequirementId;

public interface RequirementRepository extends JpaRepository<Requirement, RequirementId> {
  List<Requirement> findByShiftId(Integer shiftId);
}

package com.example.workforce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workforce.models.ShiftAssignment;
import com.example.workforce.models.keys.ShiftAssignmentId;
import java.util.List;

public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, ShiftAssignmentId> {
  List<ShiftAssignment> findByShiftId(Integer shiftId);
}

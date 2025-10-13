package com.example.workforce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workforce.models.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Integer>{
  List<Shift> findByLocationId(Integer locationId);
}

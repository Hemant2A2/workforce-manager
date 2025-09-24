package com.example.workforce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workforce.models.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Integer>{

}

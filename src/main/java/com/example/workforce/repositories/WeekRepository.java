package com.example.workforce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workforce.models.Week;
import java.time.LocalDate;
import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Integer> {
  Optional<Week> findByStartDate(LocalDate startDate);
}

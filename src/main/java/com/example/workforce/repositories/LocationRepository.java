package com.example.workforce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workforce.models.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}

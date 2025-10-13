package com.example.workforce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workforce.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  
}

package com.example.workforce.models;

import com.example.workforce.enums.Title;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Member_Types")
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor
public class MemberType {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mem_typeId")
  private Integer id;

  @Column(name = "title")
  @Enumerated(EnumType.STRING)
  private Title title;

  @Column(name = "allowed_hours")
  private Integer allowedHours;

  @Column(name = "allowed_paid_leave")
  private Integer allowedPaidLeaves;
}

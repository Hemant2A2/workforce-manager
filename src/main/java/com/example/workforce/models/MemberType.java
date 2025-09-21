package com.example.workforce.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "alllowed_hours")
  private Integer allowedHours;

  @Column(name = "allowed_paid_leaves")
  private Integer allowedPaidLeaves;
}

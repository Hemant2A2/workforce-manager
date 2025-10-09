package com.example.workforce.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.example.workforce.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Member")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Member {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "EmpId")
  private Integer id;

  @Column(name = "Fname")
  private String fName;

  @Column(name = "Mname")
  private String mName;

  @Column(name = "Lname")
  private String lName;

  @Column(name = "Gender")
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "Apartment")
  private String apartment;

  @Column(name = "city")
  private String city;

  @Column(name = "DOB")
  private LocalDate dob;

  @Column(name = "Password")
  private String password;

  @Column(name = "Overtime_required")
  private double overtimeRequired;

  @Column(name = "phone")
  private String phone;

  @Column(name = "availed_leaves")
  private Integer availedLeaves;

  @ManyToOne
  @JoinColumn(name = "locationId")
  private Location worksAt;

  @ManyToOne
  @JoinColumn(name = "mem_typeId")
  private MemberType memberType;


  @ManyToMany
  @JoinTable( // Member and Role join
    name = "Feasible_Role",
    joinColumns = @JoinColumn(name = "mem_Id"),
    inverseJoinColumns = @JoinColumn(name = "role_Id")
  )
  private Set<Role> feasibleRoles = new HashSet<>();


  @ManyToMany
  @JoinTable( // Member and Shift join
    name = "Unavailability",
    joinColumns = @JoinColumn(name = "EmpId"),
    inverseJoinColumns = @JoinColumn(name = "ShiftId")
  )
  private Set<Shift> unavailableShifts = new HashSet<>();

}

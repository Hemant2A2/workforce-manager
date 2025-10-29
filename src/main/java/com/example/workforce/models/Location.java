package com.example.workforce.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Location")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Location {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "location_Id")
  private Integer id;

  @Column(name = "plot_No")
  private String plotNo;

  @Column(name = "Street")
  private String street;

  @Column(name = "city")
  private String city;

  @Column(name = "pincode")
  private String pincode;

  @OneToMany(mappedBy = "worksAt")
  private Set<Member> members = new HashSet<>();

  @OneToOne
  @JoinColumn(name = "manager_Id")
  private Member manager;

}

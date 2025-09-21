package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String name;

    @Column(name = "Standard_Rate", nullable = false)
    private BigDecimal standardRate;

    @Column(name = "Overtime_Rate")
    private BigDecimal overtimeRate;

    @ManyToMany(mappedBy = "feasibleRoles")
    private Set<Member> members = new HashSet<>();
}

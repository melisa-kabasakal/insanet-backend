package com.insanet.insanet_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company", schema = "insanet")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false)
    @NotNull
    private String companyName;

    @Column(name = "company_full_name", nullable = false)
    @NotNull
    private String companyFullName;

    @Column(name = "tax_id", unique = true, nullable = false)
    @NotNull
    private String taxId;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<User> users;
}

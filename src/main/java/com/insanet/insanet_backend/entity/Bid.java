package com.insanet.insanet_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bids", schema = "insanet")
@Data
@NoArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "budget", nullable = false)
    private Double budget;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "contractor_id")
    private User contractor;

    @Column(name = "status", nullable = false)
    private String status;
}

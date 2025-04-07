package com.insanet.insanet_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offers", schema = "insanet")
@Data
@NoArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bid_id", nullable = false)
    private Bid bid;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private User supplier;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "delivery_time", nullable = false)
    private String deliveryTime;

    @Column(name = "status", nullable = false)
    private String status;
}

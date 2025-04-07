package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}

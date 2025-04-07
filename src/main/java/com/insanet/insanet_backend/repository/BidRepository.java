package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}

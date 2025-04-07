package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.MaterialRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Long> {
    List<MaterialRequest> findByBidId(Long bidId);
}


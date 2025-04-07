package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.Bid;
import com.insanet.insanet_backend.entity.MaterialRequest;
import com.insanet.insanet_backend.repository.BidRepository;
import com.insanet.insanet_backend.repository.MaterialRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialRequestServiceImpl implements MaterialRequestService {

    private final MaterialRequestRepository materialRequestRepository;
    private final BidRepository bidRepository;

    @Autowired
    public MaterialRequestServiceImpl(MaterialRequestRepository materialRequestRepository, BidRepository bidRepository) {
        this.materialRequestRepository = materialRequestRepository;
        this.bidRepository = bidRepository;
    }

    @Override
    public MaterialRequest createMaterialRequest(MaterialRequest materialRequest) {
        Bid bid = bidRepository.findById(materialRequest.getBid().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bid not found"));
        materialRequest.setBid(bid);
        return materialRequestRepository.save(materialRequest);
    }

    @Override
    public List<MaterialRequest> getMaterialRequestsByBid(Long bidId) {
        return materialRequestRepository.findByBidId(bidId);
    }

    @Override
    public MaterialRequest updateMaterialRequestStatus(Long id, String status) {
        MaterialRequest materialRequest = materialRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MaterialRequest not found"));
        materialRequest.setStatus(status);
        return materialRequestRepository.save(materialRequest);
    }
}


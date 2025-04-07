package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.Bid;

public interface BidService {
    Bid createBid(Bid bid);
    Bid getBidById(Long id);
    void deleteBid(Long id);
}

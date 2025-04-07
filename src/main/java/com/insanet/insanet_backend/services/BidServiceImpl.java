package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.Bid;
import com.insanet.insanet_backend.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Override
    public Bid createBid(Bid bid) {
        return bidRepository.save(bid);
    }

    @Override
    public Bid getBidById(Long id) {
        return bidRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteBid(Long id) {
        bidRepository.deleteById(id);
    }
}

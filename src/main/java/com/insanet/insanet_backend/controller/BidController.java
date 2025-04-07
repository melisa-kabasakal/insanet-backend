package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.dto.BidRequest;
import com.insanet.insanet_backend.dto.BidResponse;
import com.insanet.insanet_backend.entity.Bid;
import com.insanet.insanet_backend.exceptions.CustomException;
import com.insanet.insanet_backend.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping("/create")
    public ResponseEntity<BidResponse> createBid(@RequestBody BidRequest bidRequest) {

        Bid bid = new Bid();
        bid.setMaterialName(bidRequest.getMaterialName());
        bid.setDescription(bidRequest.getDescription());
        bid.setBudget(bidRequest.getBudget());
        bid.setStartDate(bidRequest.getStartDate());
        bid.setEndDate(bidRequest.getEndDate());
        bid.setStatus("active");

        Bid savedBid = bidService.createBid(bid);
        BidResponse response = new BidResponse(
                savedBid.getId(), savedBid.getMaterialName(), savedBid.getDescription(),
                savedBid.getBudget(), savedBid.getStartDate(), savedBid.getEndDate(), savedBid.getStatus()
        );

        return ResponseEntity.ok(response);
    }
}

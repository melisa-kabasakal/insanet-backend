package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.dto.BidDTO;
import com.insanet.insanet_backend.dto.BidListResponse;
import com.insanet.insanet_backend.dto.CreateBidRequest;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.services.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/auctions/{auctionId}/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<BidDTO> createBid(
            @PathVariable Long auctionId,
            @Valid @RequestBody CreateBidRequest request,
            @AuthenticationPrincipal User bidder) {
        return ResponseEntity.ok(bidService.createBid(auctionId, request, bidder));
    }

    @GetMapping
    public ResponseEntity<BidListResponse> getBids(
            @PathVariable Long auctionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "amount") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(bidService.getBidsForAuction(auctionId, page, size, sortBy, direction));
    }

    @GetMapping("/highest")
    public ResponseEntity<BigDecimal> getHighestBidAmount(@PathVariable Long auctionId) {
        return ResponseEntity.ok(bidService.getHighestBidAmount(auctionId));
    }

    @GetMapping("/winning")
    public ResponseEntity<BidDTO> getWinningBid(@PathVariable Long auctionId) {
        return ResponseEntity.ok(bidService.getWinningBid(auctionId));
    }
}

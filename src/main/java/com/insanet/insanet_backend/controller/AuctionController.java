package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.dto.*;
import com.insanet.insanet_backend.enums.AuctionStatus;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.services.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @GetMapping
    public ResponseEntity<AuctionListResponse> getAuctions(
            @RequestParam(defaultValue = "ACTIVE") AuctionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) List<String> products,
            @RequestParam(required = false) List<String> locations,
            @RequestParam(defaultValue = "newest") String sort) {

        Sort.Direction direction = sort.equals("price-high") || sort.equals("newest") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        String sortField = sort.startsWith("price") ? "startingPrice" :
                sort.equals("closing-soon") ? "endDate" : "createdAt";

        return ResponseEntity.ok(auctionService.getAuctions(
                status, page, size, searchTerm, products, locations, sortField, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDetailDTO> getAuctionDetail(@PathVariable Long id) {
        return ResponseEntity.ok(auctionService.getAuctionDetail(id));
    }

    @PostMapping
    public ResponseEntity<AuctionDetailDTO> createAuction(
            @Valid @RequestBody CreateAuctionRequest request,
            @AuthenticationPrincipal User contractor) {
        return ResponseEntity.ok(auctionService.createAuction(request, contractor));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateAuctionStatus(
            @PathVariable Long id,
            @RequestParam AuctionStatus status,
            @AuthenticationPrincipal User user) {
        auctionService.updateAuctionStatus(id, status, user);
        return ResponseEntity.ok().build();
    }
}

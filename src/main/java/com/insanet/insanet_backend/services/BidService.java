package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.dto.BidDTO;
import com.insanet.insanet_backend.dto.BidListResponse;
import com.insanet.insanet_backend.dto.CreateBidRequest;
import com.insanet.insanet_backend.entity.User;

import java.math.BigDecimal;

public interface BidService {

    BidDTO createBid(Long auctionId, CreateBidRequest request, User bidder);

    BidListResponse getBidsForAuction(Long auctionId, int page, int size, String sortBy, String direction);

    BigDecimal getHighestBidAmount(Long auctionId);

    BidDTO getWinningBid(Long auctionId);
}

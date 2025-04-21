package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.dto.BidDTO;
import com.insanet.insanet_backend.dto.BidListResponse;
import com.insanet.insanet_backend.dto.CreateBidRequest;
import com.insanet.insanet_backend.entity.Auction;
import com.insanet.insanet_backend.entity.Bid;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.exceptions.BadRequestException;
import com.insanet.insanet_backend.exceptions.ResourceNotFoundException;
import com.insanet.insanet_backend.repository.AuctionRepository;
import com.insanet.insanet_backend.repository.BidRepository;
import com.insanet.insanet_backend.services.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

    @Override
    @Transactional
    public BidDTO createBid(Long auctionId, CreateBidRequest request, User bidder) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + auctionId));

        if (auction.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Auction has expired");
        }

        BigDecimal currentHighestBid = bidRepository.findTopByAuctionIdOrderByAmountDesc(auctionId)
                .map(Bid::getAmount)
                .orElse(auction.getStartingPrice());

        if (request.getAmount().compareTo(currentHighestBid) <= 0) {
            throw new BadRequestException("Bid amount must be higher than current highest bid");
        }

        Bid bid = Bid.builder()
                .auction(auction)
                .bidder(bidder)
                .amount(request.getAmount())
                .note(request.getNote())
                .isWinningBid(true)
                .build();

        bidRepository.findWinningBid(auctionId)
                .ifPresent(previousWinningBid -> {
                    previousWinningBid.setWinningBid(false);
                    bidRepository.save(previousWinningBid);
                });

        bid = bidRepository.save(bid);

        return BidDTO.builder()
                .id(bid.getId())
                .auctionId(bid.getAuction().getId())
                .amount(bid.getAmount())
                .note(bid.getNote())
                .createdAt(bid.getCreatedAt())
                .bidderId(bid.getBidder().getId())
                .bidderName(bid.getBidder().getUsername())
                .isWinningBid(bid.isWinningBid())
                .build();
    }

    @Override
    public BidListResponse getBidsForAuction(Long auctionId, int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Bid> bidsPage = bidRepository.findByAuctionId(auctionId, pageable);

        Page<BidDTO> bidDTOs = bidsPage.map(bid -> BidDTO.builder()
                .id(bid.getId())
                .auctionId(bid.getAuction().getId())
                .amount(bid.getAmount())
                .note(bid.getNote())
                .createdAt(bid.getCreatedAt())
                .bidderId(bid.getBidder().getId())
                .bidderName(bid.getBidder().getUsername())
                .isWinningBid(bid.isWinningBid())
                .build());

        return BidListResponse.builder()
                .bids(bidDTOs.getContent())
                .currentPage(bidDTOs.getNumber())
                .totalPages(bidDTOs.getTotalPages())
                .totalItems(bidDTOs.getTotalElements())
                .build();
    }

    @Override
    public BigDecimal getHighestBidAmount(Long auctionId) {
        return bidRepository.findHighestBidAmount(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("No bids found for auction with id: " + auctionId));
    }

    @Override
    public BidDTO getWinningBid(Long auctionId) {
        Bid winningBid = bidRepository.findWinningBid(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("No winning bid found for auction with id: " + auctionId));

        return BidDTO.builder()
                .id(winningBid.getId())
                .auctionId(winningBid.getAuction().getId())
                .amount(winningBid.getAmount())
                .note(winningBid.getNote())
                .createdAt(winningBid.getCreatedAt())
                .bidderId(winningBid.getBidder().getId())
                .bidderName(winningBid.getBidder().getUsername())
                .isWinningBid(winningBid.isWinningBid())
                .build();
    }
}

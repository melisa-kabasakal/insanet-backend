package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.Auction;
import com.insanet.insanet_backend.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Page<Bid> findByAuctionId(Long auctionId, Pageable pageable);

    Optional<Bid> findTopByAuctionIdOrderByAmountDesc(Long auctionId);

    List<Bid> findTop5ByAuctionOrderByAmountDesc(Auction auction);

    @Query("SELECT COUNT(b) FROM Bid b WHERE b.auction = :auction")
    int countByAuction(@Param("auction") Auction auction);

    @Query("SELECT MAX(b.amount) FROM Bid b WHERE b.auction.id = :auctionId")
    Optional<BigDecimal> findHighestBidAmount(@Param("auctionId") Long auctionId);

    @Query("SELECT b FROM Bid b WHERE b.auction.id = :auctionId AND b.isWinningBid = true")
    Optional<Bid> findWinningBid(@Param("auctionId") Long auctionId);
}

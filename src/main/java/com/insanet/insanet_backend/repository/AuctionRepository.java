package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.Auction;
import com.insanet.insanet_backend.enums.AuctionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction> {
    Page<Auction> findByStatus(AuctionStatus status, Pageable pageable);

    Page<Auction> findByContractorId(Long contractorId, Pageable pageable);

    @Query("SELECT a FROM Auction a WHERE a.status = :status AND a.endDate < :now")
    List<Auction> findExpiredAuctions(@Param("status") AuctionStatus status, @Param("now") LocalDateTime now);

    @Query("SELECT a FROM Auction a WHERE a.status = :status AND a.category = :category")
    Page<Auction> findByStatusAndCategory(@Param("status") AuctionStatus status, @Param("category") String category, Pageable pageable);

    @Query("SELECT a FROM Auction a WHERE a.status = :status AND (LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Auction> searchByKeyword(@Param("status") AuctionStatus status, @Param("keyword") String keyword, Pageable pageable);
}

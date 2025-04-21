package com.insanet.insanet_backend.dto;

import com.insanet.insanet_backend.enums.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDetailDTO {
    private Long id;
    private String title;
    private String category;
    private String location;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentBid;
    private String imageUrl;
    private AuctionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String timeLeft;
    private int bidCount;
    private UserSummaryDTO contractor;
    private List<BidDTO> recentBids;
    private LocalDateTime createdAt;
}

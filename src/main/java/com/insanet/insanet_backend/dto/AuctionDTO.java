package com.insanet.insanet_backend.dto;

import com.insanet.insanet_backend.enums.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDTO {
    private Long id;
    private String title;
    private String category;
    private String location;
    private BigDecimal startingPrice;
    private BigDecimal currentBid;
    private String imageUrl;
    private AuctionStatus status;
    private LocalDateTime endDate;
    private int bidCount;
    private String timeLeft;
    private UserSummaryDTO contractor;
}

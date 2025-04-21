package com.insanet.insanet_backend.dto;

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
public class BidDTO {
    private Long id;
    private Long auctionId;
    private BigDecimal amount;
    private String note;
    private LocalDateTime createdAt;
    private Long bidderId;
    private String bidderName;
    private boolean isWinningBid;
}

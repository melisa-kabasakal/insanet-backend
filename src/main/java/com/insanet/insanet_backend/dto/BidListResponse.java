package com.insanet.insanet_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidListResponse {
    private List<BidDTO> bids;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}

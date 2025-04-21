package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.dto.*;
import com.insanet.insanet_backend.enums.AuctionStatus;
import com.insanet.insanet_backend.entity.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuctionService {
    AuctionListResponse getAuctions(
            AuctionStatus status,
            int page,
            int size,
            String searchTerm,
            List<String> products,
            List<String> locations,
            String sortField,
            Sort.Direction direction
    );

    AuctionDetailDTO getAuctionDetail(Long id);

    AuctionDetailDTO createAuction(CreateAuctionRequest request, User contractor);

    void updateAuctionStatus(Long id, AuctionStatus status, User user);
}

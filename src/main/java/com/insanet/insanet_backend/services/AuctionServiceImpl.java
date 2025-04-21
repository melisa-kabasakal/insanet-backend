package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.services.AuctionService;

import com.insanet.insanet_backend.dto.*;
import com.insanet.insanet_backend.entity.Auction;
import com.insanet.insanet_backend.enums.AuctionStatus;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.exceptions.ResourceNotFoundException;
import com.insanet.insanet_backend.exceptions.UnauthorizedException;
import com.insanet.insanet_backend.repository.AuctionRepository;
import com.insanet.insanet_backend.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Override
    public AuctionListResponse getAuctions(
            AuctionStatus status,
            int page,
            int size,
            String searchTerm,
            List<String> products,
            List<String> locations,
            String sortField,
            Sort.Direction direction) {

        Specification<Auction> spec = Specification.where(null);

        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("title")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("description")), "%" + searchTerm.toLowerCase() + "%")
                    )
            );
        }

        if (products != null && !products.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("category").in(products));
        }

        if (locations != null && !locations.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("location").in(locations));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Auction> auctionPage = auctionRepository.findAll(spec, pageable);

        List<AuctionDTO> auctionDTOs = auctionPage.getContent().stream()
                .map(this::convertToAuctionDTO)
                .collect(Collectors.toList());

        return AuctionListResponse.builder()
                .auctions(auctionDTOs)
                .currentPage(page)
                .totalPages(auctionPage.getTotalPages())
                .totalItems(auctionPage.getTotalElements())
                .build();
    }

    @Override
    public AuctionDetailDTO getAuctionDetail(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("İhale bulunamadı"));

        List<BidDTO> recentBids = bidRepository.findTop5ByAuctionOrderByAmountDesc(auction)
                .stream()
                .map(bid -> BidDTO.builder()
                        .id(bid.getId())
                        .amount(bid.getAmount())
                        .createdAt(bid.getCreatedAt())
                        .bidderId(bid.getBidder().getId())
                        .bidderName(bid.getBidder().getUsername())
                        .build())
                .collect(Collectors.toList());

        return convertToAuctionDetailDTO(auction, recentBids);
    }

    @Override
    @Transactional
    public AuctionDetailDTO createAuction(CreateAuctionRequest request, User contractor) {
        Auction auction = Auction.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .location(request.getLocation())
                .startingPrice(request.getStartingPrice())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(AuctionStatus.DRAFT)
                .contractor(contractor)
                .createdAt(LocalDateTime.now())
                .build();

        auction = auctionRepository.save(auction);
        return convertToAuctionDetailDTO(auction, List.of());
    }

    @Override
    @Transactional
    public void updateAuctionStatus(Long id, AuctionStatus status, User user) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("İhale bulunamadı"));

        if (!auction.getContractor().getId().equals(user.getId())) {
            throw new UnauthorizedException("Bu işlem için yetkiniz yok");
        }

        auction.setStatus(status);
        auctionRepository.save(auction);
    }

    private AuctionDTO convertToAuctionDTO(Auction auction) {
        return AuctionDTO.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .category(auction.getCategory())
                .location(auction.getLocation())
                .startingPrice(auction.getStartingPrice())
                .currentBid(bidRepository.findHighestBidAmount(auction.getId()).orElse(auction.getStartingPrice()))
                .imageUrl(auction.getImageUrl())
                .status(auction.getStatus())
                .endDate(auction.getEndDate())
                .bidCount(bidRepository.countByAuction(auction))
                .timeLeft(calculateTimeLeft(auction))
                .contractor(UserSummaryDTO.fromUser(auction.getContractor()))
                .build();
    }

    private AuctionDetailDTO convertToAuctionDetailDTO(Auction auction, List<BidDTO> recentBids) {
        return AuctionDetailDTO.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .category(auction.getCategory())
                .location(auction.getLocation())
                .description(auction.getDescription())
                .startingPrice(auction.getStartingPrice())
                .currentBid(bidRepository.findHighestBidAmount(auction.getId()).orElse(auction.getStartingPrice()))
                .imageUrl(auction.getImageUrl())
                .status(auction.getStatus())
                .startDate(auction.getStartDate())
                .endDate(auction.getEndDate())
                .timeLeft(calculateTimeLeft(auction))
                .bidCount(bidRepository.countByAuction(auction))
                .contractor(UserSummaryDTO.fromUser(auction.getContractor()))
                .recentBids(recentBids)
                .createdAt(auction.getCreatedAt())
                .build();
    }

    private String calculateTimeLeft(Auction auction) {
        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            return "";
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(auction.getEndDate())) {
            return "Süre doldu";
        }

        Duration duration = Duration.between(now, auction.getEndDate());
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        if (days > 0) {
            return days + " gün";
        } else if (hours > 0) {
            return hours + " saat";
        } else {
            return minutes + " dakika";
        }
    }
}

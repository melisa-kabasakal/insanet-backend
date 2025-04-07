package com.insanet.insanet_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BidResponse {

    private Long id;
    private String materialName;
    private String description;
    private Double budget;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
}

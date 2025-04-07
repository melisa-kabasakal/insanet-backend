package com.insanet.insanet_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BidRequest {

    @NotNull
    private String materialName;

    private String description;

    @NotNull
    private Double budget;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}

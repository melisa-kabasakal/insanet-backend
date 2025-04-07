package com.insanet.insanet_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OfferRequest {

    @NotNull
    private Long bidId;

    @NotNull
    private Double offerAmount;

    private String description;
}

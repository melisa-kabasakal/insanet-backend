package com.insanet.insanet_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OfferResponse {

    private Long id;
    private Long bidId;
    private Double offerAmount;
    private String description;
}

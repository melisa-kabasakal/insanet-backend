package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.Offer;

public interface OfferService {
    Offer createOffer(Offer offer);
    Offer getOfferById(Long id);
    void deleteOffer(Long id);
}

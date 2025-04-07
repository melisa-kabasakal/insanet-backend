package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.MaterialRequest;

import java.util.List;

public interface MaterialRequestService {

    MaterialRequest createMaterialRequest(MaterialRequest materialRequest);

    List<MaterialRequest> getMaterialRequestsByBid(Long bidId);

    MaterialRequest updateMaterialRequestStatus(Long materialRequestId, String status);
}

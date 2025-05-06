package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.entity.MaterialRequest;
import com.insanet.insanet_backend.services.MaterialRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material-requests")
public class MaterialRequestController {

    private final MaterialRequestService materialRequestService;

    @Autowired
    public MaterialRequestController(MaterialRequestService materialRequestService) {
        this.materialRequestService = materialRequestService;
    }

    @PostMapping("/create")
    public ResponseEntity<MaterialRequest> createMaterialRequest(@RequestBody MaterialRequest materialRequest) {
        MaterialRequest createdRequest = materialRequestService.createMaterialRequest(materialRequest);
        return ResponseEntity.ok(createdRequest);
    }


    @GetMapping("/bid/{bidId}")
    public ResponseEntity<List<MaterialRequest>> getMaterialRequestsByBid(@PathVariable Long bidId) {
        List<MaterialRequest> requests = materialRequestService.getMaterialRequestsByBid(bidId);
        return ResponseEntity.ok(requests);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MaterialRequest> updateMaterialRequestStatus(@PathVariable Long id, @RequestParam String status) {
        MaterialRequest updatedRequest = materialRequestService.updateMaterialRequestStatus(id, status);
        return ResponseEntity.ok(updatedRequest);
    }
}


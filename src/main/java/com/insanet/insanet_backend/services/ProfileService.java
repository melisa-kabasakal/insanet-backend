package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.dto.UserProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    UserProfileDTO getUserProfile(String username);
    UserProfileDTO updateProfile(String username, UserProfileDTO profileDTO, MultipartFile profilePicture,
                                 MultipartFile taxCertificate, MultipartFile tradeRegistry,
                                 MultipartFile signatureCertificate, MultipartFile chamberOfCommerce);
    UserProfileDTO updateProfilePicture(String username, MultipartFile file);
    ResponseEntity<byte[]> getDocument(String username, String documentType);
}

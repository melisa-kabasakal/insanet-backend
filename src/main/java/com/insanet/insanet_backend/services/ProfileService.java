package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.dto.UserProfileDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

public interface ProfileService {

    UserProfileDTO getUserProfile(String username);

    UserProfileDTO updateProfile(
            String username,
            UserProfileDTO profileDTO,
            MultipartFile profilePicture,
            MultipartFile taxCertificate,
            MultipartFile tradeRegistryGazette,
            MultipartFile signatureCircular,
            MultipartFile activityCertificate
    );

    UserProfileDTO updateProfilePicture(String username, MultipartFile profilePicture); // userId yerine username

    ResponseEntity<byte[]> getDocument(String username, String documentType);

    ResponseEntity<String> uploadFile(String username, MultipartFile file, String documentType);
}


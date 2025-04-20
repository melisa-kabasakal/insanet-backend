package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.dto.UserProfileDTO;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public UserProfileDTO getUserProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return profileService.getUserProfile(user.getUsername());
    }

    @PutMapping
    public UserProfileDTO updateProfile(
            Authentication authentication,
            @RequestPart(value = "profileDTO") UserProfileDTO profileDTO,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "taxCertificate", required = false) MultipartFile taxCertificate,
            @RequestPart(value = "tradeRegistry", required = false) MultipartFile tradeRegistry,
            @RequestPart(value = "signatureCertificate", required = false) MultipartFile signatureCertificate,
            @RequestPart(value = "chamberOfCommerce", required = false) MultipartFile chamberOfCommerce) {

        User user = (User) authentication.getPrincipal();
        return profileService.updateProfile(
                user.getUsername(),
                profileDTO,
                profilePicture,
                taxCertificate,
                tradeRegistry,
                signatureCertificate,
                chamberOfCommerce
        );
    }

    @PutMapping("/picture")
    public UserProfileDTO updateProfilePicture(
            Authentication authentication,
            @RequestPart("file") MultipartFile file) {
        User user = (User) authentication.getPrincipal();
        return profileService.updateProfilePicture(user.getUsername(), file);
    }

    @GetMapping("/document/{documentType}")
    public ResponseEntity<byte[]> getDocument(
            Authentication authentication,
            @PathVariable String documentType) {
        User user = (User) authentication.getPrincipal();
        return profileService.getDocument(user.getUsername(), documentType);
    }
}

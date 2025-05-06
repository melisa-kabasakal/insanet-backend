package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.dto.UserProfileDTO;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserProfileDTO updateProfile(
            Authentication authentication,
            @RequestPart("profileDTO") UserProfileDTO profileDTO,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "taxCertificate", required = false) MultipartFile taxCertificate,
            @RequestPart(value = "tradeRegistry", required = false) MultipartFile tradeRegistry,
            @RequestPart(value = "signatureCertificate", required = false) MultipartFile signatureCertificate,
            @RequestPart(value = "chamberOfCommerce", required = false) MultipartFile chamberOfCommerce
    ) {
        System.out.println("Profil DTO: " + profileDTO);
        System.out.println("Profil Resmi: " + (profilePicture != null ? profilePicture.getOriginalFilename() : "null"));
        System.out.println("Vergi Sertifikası: " + (taxCertificate != null ? taxCertificate.getOriginalFilename() : "null"));

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

    @PostMapping("/picture")
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {
        User user = (User) authentication.getPrincipal();

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dosya boş olamaz.");
        }

        System.out.println("Kullanıcı: " + user.getUsername() + ", Dosya adı: " + file.getOriginalFilename());

        return ResponseEntity.ok("Dosya başarıyla yüklendi: " + file.getOriginalFilename());
    }
}

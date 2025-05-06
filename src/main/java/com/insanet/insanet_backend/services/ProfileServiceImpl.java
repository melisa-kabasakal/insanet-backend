package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.dto.UserProfileDTO;
import com.insanet.insanet_backend.dto.DocumentsDTO;
import com.insanet.insanet_backend.entity.Profile;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.entity.Company;
import com.insanet.insanet_backend.repository.ProfileRepository;
import com.insanet.insanet_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    public UserProfileDTO getUserProfile(String username) {
        User user = userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            profile.setDocuments(new DocumentsDTO());
            profile = profileRepository.save(profile);
        }

        return convertToDTO(user, profile);
    }

    @Override
    @Transactional
    public UserProfileDTO updateProfile(
            String username,
            UserProfileDTO profileDTO,
            MultipartFile profilePicture,
            MultipartFile taxCertificate,
            MultipartFile tradeRegistryGazette,
            MultipartFile signatureCircular,
            MultipartFile activityCertificate
    ) {
        User user = userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
        }

        profile.setFullName(profileDTO.getFullName());

        // Profil resmi yüklemesi
        if (profilePicture != null) {
            String profileUrl = "https://dummyurl.com/" + profilePicture.getOriginalFilename();
            profile.setProfilePictureUrl(profileUrl);
        }

        // Belgeleri işle
        DocumentsDTO documents = profile.getDocuments();
        if (documents == null) documents = new DocumentsDTO();

        try {
            if (taxCertificate != null) {
                documents.setTaxCertificate(new String(taxCertificate.getBytes()));
            }
            if (tradeRegistryGazette != null) {
                documents.setTradeRegistry(new String(tradeRegistryGazette.getBytes()));
            }
            if (signatureCircular != null) {
                documents.setSignatureCertificate(new String(signatureCircular.getBytes()));
            }
            if (activityCertificate != null) {
                documents.setChamberOfCommerce(new String(activityCertificate.getBytes()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Belge okunurken hata oluştu", e);
        }

        profile.setDocuments(documents);

        profileRepository.save(profile);
        userRepository.save(user);

        return convertToDTO(user, profile);
    }

    @Override
    public UserProfileDTO updateProfilePicture(String username, MultipartFile profilePicture) {
        User user = userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (profilePicture != null) {
            String url = "https://dummyurl.com/" + profilePicture.getOriginalFilename();

            Profile profile = user.getProfile();
            if (profile != null) {
                profile.setProfilePictureUrl(url);
                profileRepository.save(profile);
            }
        }

        return UserProfileDTO.fromUser(user);
    }

    @Override
    public ResponseEntity<byte[]> getDocument(String username, String documentType) {
        User user = userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Profile profile = user.getProfile();
        if (profile == null || profile.getDocuments() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        DocumentsDTO docs = profile.getDocuments();
        String content = switch (documentType.toLowerCase()) {
            case "tax" -> docs.getTaxCertificate();
            case "registry" -> docs.getTradeRegistry();
            case "signature" -> docs.getSignatureCertificate();
            case "activity" -> docs.getChamberOfCommerce();
            default -> null;
        };

        return content != null
                ? ResponseEntity.ok(content.getBytes())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Override
    public ResponseEntity<String> uploadFile(String username, MultipartFile file, String documentType) {
        return ResponseEntity.ok("Dosya yüklendi (dummy cevap)");
    }

    private UserProfileDTO convertToDTO(User user, Profile profile) {
        Company company = user.getCompany();

        return UserProfileDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(profile.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .jobTitle(null)
                .companyName(company != null ? company.getCompanyName() : null)
                .companyFullName(company != null ? company.getCompanyFullName() : null)
                .taxId(company != null ? company.getTaxId() : null)
                .profilePictureUrl(profile.getProfilePictureUrl())
                .isLoggedIn(true)
                .build();
    }
}

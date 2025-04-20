package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.dto.DocumentsDTO;
import com.insanet.insanet_backend.dto.UserProfileDTO;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.exceptions.CustomException;
import com.insanet.insanet_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private final UserRepository userRepository;

    @Autowired
    public ProfileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileDTO getUserProfile(String username) {
        User user = userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhoneNumber());
        dto.setRole(user.getUserType().toString());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setDocuments(user.getDocuments());
        dto.setLoggedIn(true);
        return dto;
    }

    @Override
    public UserProfileDTO updateProfile(
            String username,
            UserProfileDTO profileDTO,
            MultipartFile profilePicture,
            MultipartFile taxCertificate,
            MultipartFile tradeRegistry,
            MultipartFile signatureCertificate,
            MultipartFile chamberOfCommerce) {

        User user = userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        updateUserFromDTO(user, profileDTO);

        DocumentsDTO documents = new DocumentsDTO();

        // Handle document uploads
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String profilePicPath = saveFile(profilePicture, "profile-pictures");
            user.setProfilePictureUrl(profilePicPath);
        }

        if (taxCertificate != null && !taxCertificate.isEmpty()) {
            documents.setTaxCertificate(saveFile(taxCertificate, "tax-certificates"));
        }
        if (tradeRegistry != null && !tradeRegistry.isEmpty()) {
            documents.setTradeRegistry(saveFile(tradeRegistry, "trade-registry"));
        }
        if (signatureCertificate != null && !signatureCertificate.isEmpty()) {
            documents.setSignatureCertificate(saveFile(signatureCertificate, "signature-certificates"));
        }
        if (chamberOfCommerce != null && !chamberOfCommerce.isEmpty()) {
            documents.setChamberOfCommerce(saveFile(chamberOfCommerce, "chamber-of-commerce"));
        }

        user = userRepository.save(user);
        UserProfileDTO updatedProfile = convertToDTO(user);
        updatedProfile.setDocuments(documents);
        return updatedProfile;
    }

    @Override
    public UserProfileDTO updateProfilePicture(String username, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException("Profile picture file is required", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        String profilePicPath = saveFile(file, "profile-pictures");
        user.setProfilePictureUrl(profilePicPath);

        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Override
    public ResponseEntity<byte[]> getDocument(String username, String documentType) {
        User user = userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        String filePath = switch (documentType.toLowerCase()) {
            case "profile-picture" -> user.getProfilePictureUrl();
            case "tax-certificate" -> user.getDocuments().getTaxCertificate();
            case "trade-registry" -> user.getDocuments().getTradeRegistry();
            case "signature-certificate" -> user.getDocuments().getSignatureCertificate();
            case "chamber-of-commerce" -> user.getDocuments().getChamberOfCommerce();
            default -> throw new CustomException("Invalid document type", HttpStatus.BAD_REQUEST);
        };

        if (filePath == null) {
            throw new CustomException("Document not found", HttpStatus.NOT_FOUND);
        }

        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new CustomException("Document file not found", HttpStatus.NOT_FOUND);
            }

            byte[] content = Files.readAllBytes(path);
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", path.getFileName().toString());

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new CustomException("Error reading file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String saveFile(MultipartFile file, String subDirectory) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir, subDirectory);
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            throw new CustomException("Could not save file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserProfileDTO convertToDTO(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhoneNumber());
        dto.setRole(user.getUserType().toString());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setDocuments(user.getDocuments());
        dto.setLoggedIn(true);
        return dto;
    }

    private void updateUserFromDTO(User user, UserProfileDTO dto) {
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            user.setPhoneNumber(dto.getPhone());
        }
    }
}

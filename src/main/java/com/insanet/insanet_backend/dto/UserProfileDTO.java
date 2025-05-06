package com.insanet.insanet_backend.dto;

import com.insanet.insanet_backend.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String jobTitle;
    private String companyName;
    private String companyFullName;
    private String taxId;

    private String profilePictureUrl;
    private boolean isLoggedIn;

    public static UserProfileDTO fromUser(User user) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getProfile() != null ? user.getProfile().getFullName() : null)
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .companyName(user.getProfile() != null ? user.getProfile().getCompanyName() : null)
                .profilePictureUrl(user.getProfile() != null ? user.getProfile().getProfilePictureUrl() : null)
                .build();
    }
}

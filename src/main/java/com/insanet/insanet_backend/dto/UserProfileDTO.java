package com.insanet.insanet_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDTO {
    private Long id;
    private String email;
    private String phone;
    private String role;
    private boolean isLoggedIn;
    private String profilePictureUrl;
    private DocumentsDTO documents;

}

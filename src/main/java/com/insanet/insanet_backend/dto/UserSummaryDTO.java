package com.insanet.insanet_backend.dto;

import com.insanet.insanet_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String companyName;
    private String role;

    public static UserSummaryDTO fromUser(User user) {
        return UserSummaryDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getProfile() != null ? user.getProfile().getFullName() : null)
                .companyName(user.getCompany() != null ? user.getCompany().getCompanyName() : null)
                .role(user.getRoles() != null && !user.getRoles().isEmpty() ? user.getRoles().iterator().next().getAuthority() : "ROLE_USER")
                .build();
    }
}

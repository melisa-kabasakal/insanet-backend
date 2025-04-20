package com.insanet.insanet_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Token cannot be empty")
    private String token;

    @NotBlank(message = "New password cannot be empty")
    private String newPassword;

}

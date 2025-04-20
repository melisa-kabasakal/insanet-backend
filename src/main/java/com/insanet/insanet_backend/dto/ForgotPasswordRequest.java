package com.insanet.insanet_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ForgotPasswordRequest {

    @NotBlank(message = "Email or Phone number cannot be empty")
    private String emailOrPhone;

}

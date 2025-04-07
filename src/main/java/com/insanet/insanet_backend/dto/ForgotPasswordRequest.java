package com.insanet.insanet_backend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "Email or Phone number cannot be empty")
    private String emailOrPhone;
    }

package com.insanet.insanet_backend.dto;

import com.insanet.insanet_backend.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    @NotBlank(message = "OTP cannot be empty")
    private String otpCode;
    private UserType userType;
    private String emailOrPhone;
}


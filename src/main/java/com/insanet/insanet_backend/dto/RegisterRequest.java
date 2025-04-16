package com.insanet.insanet_backend.dto;

import com.insanet.insanet_backend.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Contact info cannot be empty")
    private String emailOrPhone;

    @NotBlank(message = "OTP code cannot be empty")
    private String otpCode;

    private UserType userType;

    public void validate() {
        if (emailOrPhone == null || emailOrPhone.isEmpty()) {
            throw new IllegalArgumentException("E-posta veya telefon numarası boş olamaz");
        }

        if (emailOrPhone.contains("@")) {
            if (!emailOrPhone.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Geçersiz e-posta formatı");
            }
        } else {
            if (!emailOrPhone.matches("^(\\+?\\d{1,3}[- ]?)?\\(?\\d{1,4}\\)?[- ]?\\d{1,4}[- ]?\\d{1,4}$")) {
                throw new IllegalArgumentException("Geçersiz telefon numarası formatı");
            }
        }
    }
}

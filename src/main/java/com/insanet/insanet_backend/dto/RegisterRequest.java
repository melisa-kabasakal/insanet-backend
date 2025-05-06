package com.insanet.insanet_backend.dto;

import com.insanet.insanet_backend.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Contact info cannot be empty")
    private String emailOrPhone;

    private String otpCode;


    @NotNull(message = "User type is required")
    private UserType userType;

    public void validate() {
        if (emailOrPhone == null || emailOrPhone.isEmpty()) {
            throw new IllegalArgumentException("E-posta veya telefon numarası boş olamaz");
        }

        if (emailOrPhone.contains("@")) {
            validateEmail(emailOrPhone);
        } else {
            validatePhone(emailOrPhone);
        }
    }

    private void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Geçersiz e-posta formatı");
        }
    }

    private void validatePhone(String phone) {
        if (!phone.matches("^5\\d{9}$")) {
            throw new IllegalArgumentException("Geçersiz telefon numarası formatı");
        }
    }

}

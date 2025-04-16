package com.insanet.insanet_backend.services;

public interface OtpService {
    boolean sendOtpToEmail(String email, String otp);
    boolean sendOtpToPhone(String phone, String otp);
    boolean verifyOtp(String emailOrPhone, String otpCode);
}

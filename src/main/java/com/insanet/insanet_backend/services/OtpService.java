package com.insanet.insanet_backend.services;

public interface OtpService {
    boolean sendOtp(String emailOrPhone);
    boolean verifyOtp(String emailOrPhone, String otpCode);
    String getOtpForEmailOrPhone(String emailOrPhone);
}

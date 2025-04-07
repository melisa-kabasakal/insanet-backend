package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.User;

public interface AuthService {
    User register(String emailOrPhone, String password);
    String forgotPassword(String emailOrPhone);
    User resetPassword(String token, String newPassword);
    String sendPasswordResetToken(String emailOrPhone);
}

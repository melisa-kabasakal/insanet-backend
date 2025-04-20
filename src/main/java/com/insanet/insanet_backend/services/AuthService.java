package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.enums.UserType;

public interface AuthService {
    User register(String emailOrPhone, String password, UserType userType);
    String forgotPassword(String emailOrPhone);
    User resetPassword(String token, String newPassword, String emailOrPhone); // emailOrPhone parametresi eklendi
    String sendPasswordResetToken(String emailOrPhone);
}

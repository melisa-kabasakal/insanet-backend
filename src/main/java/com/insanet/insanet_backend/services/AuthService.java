package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.enums.UserType;

public interface AuthService {
    User register(String emailOrPhone, String password, UserType userType); // userType parametresi ekleniyor
    String forgotPassword(String emailOrPhone);
    User resetPassword(String token, String newPassword);
    String sendPasswordResetToken(String emailOrPhone);
}

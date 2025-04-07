package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.User;

public interface AuthService {
    User register(String username, String password);
}

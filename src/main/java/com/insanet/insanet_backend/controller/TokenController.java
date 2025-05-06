package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public TokenController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            boolean isValid = jwtTokenProvider.validateToken(tokenValue);

            if (isValid) {
                return ResponseEntity.ok("Token geçerli");
            } else {
                return ResponseEntity.status(401).body("Geçersiz token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Token doğrulama hatası");
        }
    }
}


package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.dto.LoginRequest;
import com.insanet.insanet_backend.dto.JwtResponse;
import com.insanet.insanet_backend.dto.RegisterRequest;
import com.insanet.insanet_backend.dto.RegisterResponse;
import com.insanet.insanet_backend.entity.Role;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.services.AuthService;
import com.insanet.insanet_backend.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.Set;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        User user = authService.register(request.getUsername(), request.getPassword());
        return new RegisterResponse(user.getEmail(), "User successfully registered");
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Set<Role> roles = ((User) userDetails).getRoles();

        String token = jwtTokenProvider.generateToken(userDetails.getUsername(), roles);
        return new JwtResponse(token);
    }
}

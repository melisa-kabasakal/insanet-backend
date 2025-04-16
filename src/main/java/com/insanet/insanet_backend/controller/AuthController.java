package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.dto.*;
import com.insanet.insanet_backend.entity.Role;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.services.AuthService;
import com.insanet.insanet_backend.services.OtpService;
import com.insanet.insanet_backend.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthService authService,
                          OtpService otpService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.otpService = otpService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        request.validate();

        if (!otpService.verifyOtp(request.getEmailOrPhone(), request.getOtpCode())) {
            return new RegisterResponse("Error", "Invalid OTP");
        }

        User user = authService.register(
                request.getEmailOrPhone(),
                request.getPassword(),
                request.getUserType()
        );

        return new RegisterResponse(user.getEmail() != null ? user.getEmail() : user.getPhoneNumber(), "User successfully registered");
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmailOrPhone(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Set<Role> roles = ((User) userDetails).getRoles();

        String token = jwtTokenProvider.generateToken(userDetails.getUsername(), roles);
        return new JwtResponse(token);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String resetToken = authService.sendPasswordResetToken(request.getEmailOrPhone());
        return resetToken;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return "Password successfully reset.";
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody RegisterRequest request) {
        String emailOrPhone = request.getEmailOrPhone();

        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Email veya telefon numarası boş olamaz.", false));
        }

        String otp = generateOtp();
        boolean sentSuccessfully;

        if (emailOrPhone.contains("@")) {
            sentSuccessfully = otpService.sendOtpToEmail(emailOrPhone, otp);
            String responseMessage = sentSuccessfully ? "OTP sent to email" : "Error sending OTP to email";
            return sentSuccessfully ? ResponseEntity.ok(new ApiResponse(responseMessage, true)) :
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponse(responseMessage, false));
        } else {
            sentSuccessfully = otpService.sendOtpToPhone(emailOrPhone, otp);
            String responseMessage = sentSuccessfully ? "OTP sent to phone" : "Error sending OTP to phone";
            return sentSuccessfully ? ResponseEntity.ok(new ApiResponse(responseMessage, true)) :
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponse(responseMessage, false));
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {
        boolean isOtpValid = otpService.verifyOtp(otpRequest.getEmailOrPhone(), otpRequest.getOtpCode());

        if (isOtpValid) {
            return ResponseEntity.ok(new ApiResponse("OTP verified successfully", true));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Invalid OTP", false));
        }
    }

    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 10000));
    }
}

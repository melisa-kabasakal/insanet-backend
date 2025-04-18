package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.Role;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.enums.UserType;
import com.insanet.insanet_backend.exceptions.UserAlreadyRegisteredException;
import com.insanet.insanet_backend.exceptions.UserNotFoundException;
import com.insanet.insanet_backend.repository.RoleRepository;
import com.insanet.insanet_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpServiceImpl otpService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, OtpServiceImpl otpService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
    }

    @Override
    public User register(String emailOrPhone, String password, UserType userType) {
        if (userType == null) {
            throw new IllegalArgumentException("User type cannot be null");
        }

        Optional<User> existingUser = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone);

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists with the provided email or phone number");
        }

        User user = new User();

        if (emailOrPhone.contains("@")) {
            user.setEmail(emailOrPhone);
            user.setPhoneNumber(null);
        } else {
            user.setPhoneNumber(emailOrPhone);
            user.setEmail(null);
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setUserType(userType);

        Optional<Role> userRole = roleRepository.findByAuthority("ROLE_" + userType.name());
        if (userRole.isEmpty()) {
            Role newRole = new Role();
            newRole.setAuthority("ROLE_" + userType.name());
            userRole = Optional.of(roleRepository.save(newRole));
        }
        user.setRoles(Set.of(userRole.get()));

        return userRepository.save(user);
    }

    @Override
    public User resetPassword(String token, String newPassword) {
        Optional<User> user = userRepository.findByPasswordResetToken(token);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Invalid password reset token");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);

        User resetUser = user.get();
        resetUser.setPassword(encodedPassword);
        resetUser.setPasswordResetToken(null);

        return userRepository.save(resetUser);
    }

    @Override
    public String sendPasswordResetToken(String emailOrPhone) {
        Optional<User> user = findUserByEmailOrPhone(emailOrPhone);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with provided email or phone number");
        }

        String resetToken = UUID.randomUUID().toString();
        user.get().setPasswordResetToken(resetToken);
        userRepository.save(user.get());

        return resetToken;
    }

    private Optional<User> findUserByEmailOrPhone(String emailOrPhone) {
        return userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone);
    }

    @Override
    public String forgotPassword(String emailOrPhone) {
        Optional<User> user = findUserByEmailOrPhone(emailOrPhone);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with provided email or phone number");
        }
        String resetToken = UUID.randomUUID().toString();
        user.get().setPasswordResetToken(resetToken);

        userRepository.save(user.get());

        return resetToken;
    }
}

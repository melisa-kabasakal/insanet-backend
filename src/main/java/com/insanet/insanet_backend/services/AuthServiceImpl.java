package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.Role;
import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.exceptions.UserAlreadyRegisteredException;
import com.insanet.insanet_backend.repository.RoleRepository;
import com.insanet.insanet_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private  UserRepository userRepository;
    private  RoleRepository roleRepository;
    private  PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String password) {

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            throw new UserAlreadyRegisteredException("Username already registered");
        }

        String encodePassword = passwordEncoder.encode(password);

        Optional<Role> userRole = roleRepository.findByAuthority("USER");

        if (userRole.isEmpty()) {
            Role role = new Role();
            role.setAuthority("USER");
            userRole = Optional.of(roleRepository.save(role));
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setRoles(Set.of(userRole.get()));

        return userRepository.save(user);
    }
}

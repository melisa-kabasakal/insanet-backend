package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.User;
import com.insanet.insanet_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException {
        User user;

        if (emailOrPhone.contains("@")) {
            user = userRepository.findByEmail(emailOrPhone)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + emailOrPhone));
        } else {
            user = userRepository.findByPhoneNumber(emailOrPhone)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + emailOrPhone));
        }

        return user;
    }
}

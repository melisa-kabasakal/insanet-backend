package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPasswordResetToken(String token);

    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);



}

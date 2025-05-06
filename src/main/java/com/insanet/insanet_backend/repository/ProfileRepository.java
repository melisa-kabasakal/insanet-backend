package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}


package com.mininetflix.ministreaming.infrastructure.userprofile.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mininetflix.ministreaming.infrastructure.userprofile.entity.UserProfileEntity;

public interface UserProfileJpaRepository
        extends JpaRepository<UserProfileEntity, UUID> {

    Optional<UserProfileEntity> findByUserId(UUID userId);

}

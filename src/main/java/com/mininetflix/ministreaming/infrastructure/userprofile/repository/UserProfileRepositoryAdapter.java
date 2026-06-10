package com.mininetflix.ministreaming.infrastructure.userprofile.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.userprofile.UserProfile;
import com.mininetflix.ministreaming.infrastructure.userprofile.entity.UserProfileEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter
        implements UserProfileRepository {

    private final UserProfileJpaRepository jpaRepository;

    @Override
    public UserProfile save(UserProfile profile) {

        return jpaRepository
                .save(UserProfileEntity.fromDomain(profile))
                .toDomain();
    }

    @Override
    public Optional<UserProfile> findById(UUID profileId) {

        return jpaRepository
                .findById(profileId)
                .map(UserProfileEntity::toDomain);
    }

    @Override
    public Optional<UserProfile> findByUserId(UUID userId) {

        return jpaRepository.findByUserId(userId)

                .map(UserProfileEntity::toDomain);

    }

    @Override
    public void delete(UUID profileId) {

        jpaRepository.deleteById(profileId);
    }
}

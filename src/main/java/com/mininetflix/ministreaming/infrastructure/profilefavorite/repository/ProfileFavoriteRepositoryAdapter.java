package com.mininetflix.ministreaming.infrastructure.profilefavorite.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.mininetflix.ministreaming.application.profilefavorite.port.ProfileFavoriteRepository;
import com.mininetflix.ministreaming.domain.profilefavorite.ProfileFavorite;
import com.mininetflix.ministreaming.infrastructure.profilefavorite.entity.ProfileFavoriteEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProfileFavoriteRepositoryAdapter
                implements ProfileFavoriteRepository {

        private final ProfileFavoriteJpaRepository jpaRepository;

        @Override
        public void save(ProfileFavorite favorite) {

                jpaRepository.save(
                                toEntity(favorite));
        }

        @Override
        public boolean existsByProfileAndVideo(
                        UUID profileId,
                        String videoId) {

                return jpaRepository.existsByProfileIdAndVideoId(
                                profileId,
                                videoId);
        }

        @Override
        public List<ProfileFavorite> findByProfile(
                        UUID profileId) {

                return jpaRepository
                                .findByProfileId(profileId)
                                .stream()
                                .map(this::toDomain)
                                .toList();
        }

        @Override
        public void deleteByProfileAndVideo(
                        UUID profileId,
                        String videoId) {

                jpaRepository.deleteByProfileIdAndVideoId(
                                profileId,
                                videoId);
        }

        private ProfileFavorite toDomain(
                        ProfileFavoriteEntity entity) {

                return new ProfileFavorite(
                                entity.getId(),
                                entity.getProfileId(),
                                entity.getVideoId(),
                                entity.getCreatedAt());
        }

        private ProfileFavoriteEntity toEntity(
                        ProfileFavorite favorite) {

                ProfileFavoriteEntity entity = new ProfileFavoriteEntity();

                entity.setId(
                                favorite.getId());

                entity.setProfileId(
                                favorite.getProfileId());

                entity.setVideoId(
                                favorite.getVideoId());

                entity.setCreatedAt(
                                favorite.getCreatedAt());

                return entity;
        }
}
package com.mininetflix.ministreaming.application.profilefavorite.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.profilefavorite.port.ProfileFavoriteRepository;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RemoveFavoriteUseCaseImpl
                implements RemoveFavoriteUseCase {

        private final ProfileFavoriteRepository repository;
        private final UserProfileRepository profileRepository;

        @Override
        public void execute(
                        UUID userId,
                        String videoId) {

                UUID profileId = profileRepository
                                .findByUserId(userId)
                                .orElseThrow(() -> new IllegalArgumentException("Profile not found"))
                                .getId();

                repository.deleteByProfileAndVideo(
                                profileId,
                                videoId);
        }
}
package com.mininetflix.ministreaming.application.userprofile.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileOutput;
import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileRequest;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.userprofile.UserProfile;
import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateProfileUseCaseImpl
                implements UpdateProfileUseCase {

        private final UserProfileRepository repository;

        @Override
        public UserProfileResponse execute(
                        UUID userId,
                        UpdateProfileRequest request) {

                UserProfile profile = repository
                                .findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Profile not found"));

                profile.updateProfile(
                                request.nickname(),
                                request.bio());

                UserProfile saved = repository.save(profile);

                return new UserProfileResponse(
                                saved.getUserId(),
                                saved.getNickname(),
                                saved.getAvatarKey(),
                                saved.getBio());
        }
}
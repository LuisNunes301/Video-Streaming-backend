package com.mininetflix.ministreaming.application.userprofile.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileOutput;
import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileRequest;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.userprofile.UserProfile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateProfileUseCaseImpl
                implements UpdateProfileUseCase {

        private final UserProfileRepository profileRepository;
        private final VideoStorageService storageService;

        @Override
        public UpdateProfileOutput execute(
                        String userId,
                        UpdateProfileRequest request) {

                UUID id = UUID.fromString(userId);

                UserProfile profile = profileRepository
                                .findByUserId(id)
                                .orElseThrow(() -> new RuntimeException("Profile not found"));

                profile.updateProfile(
                                request.nickname(),
                                request.bio());

                profileRepository.save(profile);

                String avatarUrl = profile.getAvatarKey() == null
                                ? null
                                : storageService.generatePublicUrl(
                                                profile.getAvatarKey());

                return new UpdateProfileOutput(
                                profile.getNickname(),
                                avatarUrl,
                                profile.getBio());
        }
}
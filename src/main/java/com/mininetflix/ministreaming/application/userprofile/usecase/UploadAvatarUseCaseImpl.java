package com.mininetflix.ministreaming.application.userprofile.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.userprofile.port.AvatarStorageService;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.userprofile.UserProfile;
import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadAvatarUseCaseImpl
        implements UploadAvatarUseCase {

    private final UserProfileRepository repository;
    private final AvatarStorageService avatarStorageService;

    @Override
    public UserProfileResponse execute(
            UUID userId,
            MultipartFile file) {

        UserProfile profile = repository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (profile.getAvatarKey() != null) {

            avatarStorageService.deleteAvatar(
                    profile.getAvatarKey());
        }

        String avatarKey = avatarStorageService.uploadAvatar(file);

        profile.updateAvatar(avatarKey);

        UserProfile saved = repository.save(profile);

        return new UserProfileResponse(
                saved.getUserId(),
                saved.getNickname(),
                saved.getAvatarKey(),
                saved.getBio());
    }
}
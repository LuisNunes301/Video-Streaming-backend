package com.mininetflix.ministreaming.application.userprofile.usecase;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;

public interface UploadAvatarUseCase {

    UserProfileResponse execute(
            UUID userId,
            MultipartFile file);
}
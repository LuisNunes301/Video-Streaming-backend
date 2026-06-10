package com.mininetflix.ministreaming.application.userprofile.usecase;

import java.util.UUID;

import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileRequest;
import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;

public interface UpdateProfileUseCase {

    UserProfileResponse execute(
            UUID userId,
            UpdateProfileRequest request);
}

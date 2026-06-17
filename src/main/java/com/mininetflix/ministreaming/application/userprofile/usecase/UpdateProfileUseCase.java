package com.mininetflix.ministreaming.application.userprofile.usecase;

import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileRequest;
import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;
import java.util.UUID;

public interface UpdateProfileUseCase {

  UserProfileResponse execute(UUID userId, UpdateProfileRequest request);
}

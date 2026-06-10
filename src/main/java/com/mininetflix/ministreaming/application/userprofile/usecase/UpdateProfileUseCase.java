package com.mininetflix.ministreaming.application.userprofile.usecase;

import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileOutput;
import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileRequest;

public interface UpdateProfileUseCase {

    UpdateProfileOutput execute(
            String userId,
            UpdateProfileRequest request);
}

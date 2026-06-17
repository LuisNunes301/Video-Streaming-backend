package com.mininetflix.ministreaming.application.userprofile.usecase;

import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;

public interface GetUserProfileUseCase {
  UserProfileResponse execute(String userId);
}

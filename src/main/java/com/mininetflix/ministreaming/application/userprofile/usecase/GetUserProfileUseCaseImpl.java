package com.mininetflix.ministreaming.application.userprofile.usecase;

import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.userprofile.UserProfile;
import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCaseImpl implements GetUserProfileUseCase {

  private final UserProfileRepository repository;

  @Override
  public UserProfileResponse execute(String userId) {

    UUID id = UUID.fromString(userId);

    UserProfile profile =
        repository.findByUserId(id).orElseThrow(() -> new RuntimeException("Profile not found"));

    return new UserProfileResponse(
        profile.getUserId(), profile.getNickname(), profile.getAvatarKey(), profile.getBio());
  }
}

package com.mininetflix.ministreaming.application.profilefavorite.usecase;

import com.mininetflix.ministreaming.application.profilefavorite.port.ProfileFavoriteRepository;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.profilefavorite.ProfileFavorite;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddFavoriteUseCaseImpl implements AddFavoriteUseCase {

  private final ProfileFavoriteRepository repository;
  private final UserProfileRepository profileRepository;

  @Override
  public void execute(UUID userId, String videoId) {

    UUID profileId =
        profileRepository
            .findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Profile not found"))
            .getId();

    boolean alreadyExists = repository.existsByProfileAndVideo(profileId, videoId);

    if (alreadyExists) {
      return;
    }

    repository.save(
        new ProfileFavorite(UUID.randomUUID(), profileId, videoId, LocalDateTime.now()));
  }
}

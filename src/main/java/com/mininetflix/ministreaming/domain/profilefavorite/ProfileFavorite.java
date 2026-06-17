package com.mininetflix.ministreaming.domain.profilefavorite;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProfileFavorite {

  private final UUID id;

  private final UUID profileId;

  private final String videoId;

  private final LocalDateTime createdAt;

  public ProfileFavorite(UUID id, UUID profileId, String videoId, LocalDateTime createdAt) {

    this.id = id;
    this.profileId = profileId;
    this.videoId = videoId;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public UUID getProfileId() {
    return profileId;
  }

  public String getVideoId() {
    return videoId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}

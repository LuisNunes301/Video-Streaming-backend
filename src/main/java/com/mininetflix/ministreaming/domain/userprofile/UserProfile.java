package com.mininetflix.ministreaming.domain.userprofile;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserProfile {

  private final UUID id;
  private final UUID userId;

  private String nickname;
  private String avatarKey;
  private String bio;

  private final LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public UserProfile(
      UUID id,
      UUID userId,
      String nickname,
      String avatarKey,
      String bio,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {

    this.id = id;
    this.userId = userId;
    this.nickname = nickname;
    this.avatarKey = avatarKey;
    this.bio = bio;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static UserProfile create(UUID userId, String nickname) {

    LocalDateTime now = LocalDateTime.now();

    return new UserProfile(UUID.randomUUID(), userId, nickname, null, null, now, now);
  }

  public void updateProfile(String nickname, String bio) {

    if (nickname != null && !nickname.isBlank()) {
      this.nickname = nickname;
    }

    this.bio = bio;

    this.updatedAt = LocalDateTime.now();
  }

  public void updateAvatar(String avatarKey) {

    this.avatarKey = avatarKey;
    this.updatedAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getNickname() {
    return nickname;
  }

  public String getAvatarKey() {
    return avatarKey;
  }

  public String getBio() {
    return bio;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}

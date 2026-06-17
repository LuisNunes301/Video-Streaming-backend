package com.mininetflix.ministreaming.infrastructure.userprofile.entity;

import com.mininetflix.ministreaming.domain.userprofile.UserProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfileEntity {

  @Id private UUID id;

  @Column(name = "user_id", nullable = false, unique = true)
  private UUID userId;

  @Column(nullable = false)
  private String nickname;

  private String avatarKey;

  @Column(length = 500)
  private String bio;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public UserProfileEntity() {}

  public static UserProfileEntity fromDomain(UserProfile profile) {

    UserProfileEntity entity = new UserProfileEntity();

    entity.id = profile.getId();
    entity.userId = profile.getUserId();
    entity.nickname = profile.getNickname();
    entity.avatarKey = profile.getAvatarKey();
    entity.bio = profile.getBio();
    entity.createdAt = profile.getCreatedAt();
    entity.updatedAt = profile.getUpdatedAt();

    return entity;
  }

  public UserProfile toDomain() {

    return new UserProfile(id, userId, nickname, avatarKey, bio, createdAt, updatedAt);
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getAvatarKey() {
    return avatarKey;
  }

  public void setAvatarKey(String avatarKey) {
    this.avatarKey = avatarKey;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}

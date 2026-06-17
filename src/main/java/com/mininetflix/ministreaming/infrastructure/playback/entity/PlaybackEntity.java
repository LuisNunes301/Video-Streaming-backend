package com.mininetflix.ministreaming.infrastructure.playback.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "playback")
public class PlaybackEntity {

  @Id private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "content_id", nullable = false)
  private UUID contentId;

  @Column(name = "progress_seconds", nullable = false)
  private double currentTime;

  @Column(nullable = false)
  private boolean completed;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private boolean completionRegistered;

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

  public UUID getContentId() {
    return contentId;
  }

  public void setContentId(UUID contentId) {
    this.contentId = contentId;
  }

  public double getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(double currentTime) {
    this.currentTime = currentTime;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public boolean isCompletionRegistered() {
    return completionRegistered;
  }

  public void setCompletionRegistered(boolean completionRegistered) {

    this.completionRegistered = completionRegistered;
  }
}

package com.mininetflix.ministreaming.domain.playback;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaybackState {

  private String userId;
  private String contentId;

  private double currentTime;
  private boolean completed;
  private boolean completionRegistered;

  private Instant lastUpdated;

  public PlaybackState(String userId, String contentId) {

    this.userId = userId;
    this.contentId = contentId;

    this.currentTime = 0.0;

    this.completed = false;

    this.completionRegistered = false;

    this.lastUpdated = Instant.now();
  }

  public void updateProgress(double newTime, double officialDuration) {

    if (newTime < 0) {
      throw new IllegalArgumentException("Progress cannot be negative");
    }

    this.currentTime = Math.min(newTime, officialDuration);

    this.completed = officialDuration > 0 && this.currentTime >= officialDuration * 0.95;

    this.lastUpdated = Instant.now();
  }
}

package com.mininetflix.ministreaming.web.controller.playback.dto;

public class PlaybackProgressRequest {

  private String contentId;
  private Double currentTime;
  private Double duration;

  public void setDuration(Double duration) {
    this.duration = duration;
  }

  public Double getDuration() {
    return duration;
  }

  public String getContentId() {
    return contentId;
  }

  public Double getCurrentTime() {
    return currentTime;
  }

  public void setContentId(String contentId) {
    this.contentId = contentId;
  }

  public void setCurrentTime(Double currentTime) {
    this.currentTime = currentTime;
  }
}

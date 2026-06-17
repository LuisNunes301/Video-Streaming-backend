package com.mininetflix.ministreaming.domain.playback.exception;

public class VideoNotFoundException extends RuntimeException {

  public VideoNotFoundException(String urlVideo) {
    super("Not found video: " + urlVideo);
  }
}

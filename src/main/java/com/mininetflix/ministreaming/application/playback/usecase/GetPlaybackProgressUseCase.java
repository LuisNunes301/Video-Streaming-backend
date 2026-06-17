package com.mininetflix.ministreaming.application.playback.usecase;

import com.mininetflix.ministreaming.domain.playback.PlaybackState;
import java.util.Optional;

public interface GetPlaybackProgressUseCase {
  Optional<PlaybackState> execute(String userId, String contentId);
}

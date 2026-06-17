package com.mininetflix.ministreaming.application.playback.usecase;

import com.mininetflix.ministreaming.application.playback.dto.SavePlaybackProgressInput;

public interface SavePlaybackProgressUseCase {
  void execute(SavePlaybackProgressInput input);
}

package com.mininetflix.ministreaming.application.playback.usecase;

import com.mininetflix.ministreaming.application.playback.dto.StartPlaybackOutput;

public interface StartPlaybackUseCase {
  StartPlaybackOutput execute(String userId, String contentId);
}

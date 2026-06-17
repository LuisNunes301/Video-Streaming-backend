package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.domain.content.event.VideoUploadedEvent;

public interface ProcessVideoUseCase {
  void execute(VideoUploadedEvent input);
}

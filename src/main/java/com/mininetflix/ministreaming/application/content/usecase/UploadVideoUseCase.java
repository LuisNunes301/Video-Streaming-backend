package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.application.content.dto.UploadVideoInput;
import com.mininetflix.ministreaming.application.content.dto.UploadVideoOutput;

public interface UploadVideoUseCase {
  UploadVideoOutput execute(UploadVideoInput input);
}

package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import java.util.List;

public interface ListVideosUseCase {
  List<VideoResponse> execute();
}

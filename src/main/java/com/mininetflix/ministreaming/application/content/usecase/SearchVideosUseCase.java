package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import java.util.List;

public interface SearchVideosUseCase {
  List<VideoResponse> execute(String query);
}

package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.domain.content.VideoCategory;
import java.util.List;

public interface ListVideosByCategoryUseCase {

  List<VideoResponse> execute(VideoCategory category);
}

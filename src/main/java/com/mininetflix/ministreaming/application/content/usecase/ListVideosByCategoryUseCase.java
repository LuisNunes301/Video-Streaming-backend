package com.mininetflix.ministreaming.application.content.usecase;

import java.util.List;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.domain.content.VideoCategory;

public interface ListVideosByCategoryUseCase {

    List<VideoResponse> execute(VideoCategory category);
}

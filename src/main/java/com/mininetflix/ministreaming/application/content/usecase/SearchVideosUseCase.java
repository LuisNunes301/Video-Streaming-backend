package com.mininetflix.ministreaming.application.content.usecase;

import java.util.List;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;

public interface SearchVideosUseCase {
    List<VideoResponse> execute(String query);

}

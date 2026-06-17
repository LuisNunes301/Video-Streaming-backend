package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;

public interface GetVideoByIdUseCase {

    VideoResponse execute(
            String videoId);
}

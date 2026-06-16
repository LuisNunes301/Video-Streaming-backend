package com.mininetflix.ministreaming.application.statistics.usecase;

import com.mininetflix.ministreaming.application.statistics.dto.VideoStatisticsResponse;

public interface GetVideoStatisticsUseCase {

    VideoStatisticsResponse execute(
            String videoId);
}
package com.mininetflix.ministreaming.application.statistics.usecase;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.statistics.dto.VideoStatisticsResponse;
import com.mininetflix.ministreaming.application.statistics.port.VideoStatisticsRepository;
import com.mininetflix.ministreaming.domain.statistics.VideoStatistics;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetVideoStatisticsUseCaseImpl
        implements GetVideoStatisticsUseCase {

    private final VideoStatisticsRepository repository;

    @Override
    public VideoStatisticsResponse execute(
            String videoId) {

        VideoStatistics statistics = repository.findByVideoId(videoId)
                .orElseThrow();

        return new VideoStatisticsResponse(
                statistics.getVideoId(),
                statistics.getViews(),
                statistics.getCompletedViews(),
                statistics.getWatchedSeconds());
    }
}
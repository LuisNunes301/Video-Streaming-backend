package com.mininetflix.ministreaming.application.statistics.port;

import java.util.Optional;

import com.mininetflix.ministreaming.domain.statistics.VideoStatistics;

public interface VideoStatisticsRepository {

        Optional<VideoStatistics> findByVideoId(
                        String videoId);

        void save(
                        VideoStatistics statistics);
}
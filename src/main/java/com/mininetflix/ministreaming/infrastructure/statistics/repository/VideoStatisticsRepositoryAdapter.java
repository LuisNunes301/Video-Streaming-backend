package com.mininetflix.ministreaming.infrastructure.statistics.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.mininetflix.ministreaming.application.statistics.port.VideoStatisticsRepository;
import com.mininetflix.ministreaming.domain.statistics.VideoStatistics;
import com.mininetflix.ministreaming.infrastructure.statistics.entity.VideoStatisticsEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VideoStatisticsRepositoryAdapter
                implements VideoStatisticsRepository {

        private final JpaVideoStatisticsRepository repository;

        @Override
        public Optional<VideoStatistics> findByVideoId(
                        String videoId) {

                return repository.findById(videoId)
                                .map(this::toDomain);
        }

        @Override
        public void save(
                        VideoStatistics statistics) {

                VideoStatisticsEntity entity = repository.findById(
                                statistics.getVideoId())
                                .orElse(
                                                new VideoStatisticsEntity());

                entity.setVideoId(
                                statistics.getVideoId());

                entity.setViews(
                                statistics.getViews());

                entity.setCompletedViews(
                                statistics.getCompletedViews());

                entity.setWatchedSeconds(
                                statistics.getWatchedSeconds());

                repository.save(entity);
        }

        private VideoStatistics toDomain(
                        VideoStatisticsEntity entity) {

                return VideoStatistics.restore(
                                entity.getVideoId(),
                                entity.getViews(),
                                entity.getCompletedViews(),
                                entity.getWatchedSeconds());
        }
}
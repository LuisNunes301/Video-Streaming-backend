package com.mininetflix.ministreaming.application.home.usecase;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.home.dto.TrendingVideoResponse;
import com.mininetflix.ministreaming.domain.content.VideoContent;
import com.mininetflix.ministreaming.infrastructure.statistics.entity.VideoStatisticsEntity;
import com.mininetflix.ministreaming.infrastructure.statistics.repository.JpaVideoStatisticsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTrendingVideosUseCaseImpl
                implements GetTrendingVideosUseCase {

        private final JpaVideoStatisticsRepository statisticsRepository;
        private final VideoCatalogRepository videoRepository;
        private final VideoStorageService videoStorageService;

        @Override
        public List<TrendingVideoResponse> execute() {

                return statisticsRepository
                                .findTop20ByOrderByViewsDescCompletedViewsDesc()
                                .stream()
                                .map(this::toResponse)
                                .filter(Objects::nonNull)
                                .toList();
        }

        private TrendingVideoResponse toResponse(
                        VideoStatisticsEntity statistics) {

                VideoContent video = videoRepository
                                .findById(statistics.getVideoId())
                                .orElse(null);

                if (video == null || !video.isActive()) {
                        return null;
                }

                String thumbnailUrl = videoStorageService.generatePublicUrl(
                                video.getThumbnailKey());

                return new TrendingVideoResponse(
                                video.getId(),
                                video.getTitle(),
                                thumbnailUrl,
                                video.getDuration(),
                                video.getCategory(),
                                statistics.getViews(),
                                statistics.getCompletedViews());
        }
}

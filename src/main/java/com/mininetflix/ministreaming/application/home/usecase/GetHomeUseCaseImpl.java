package com.mininetflix.ministreaming.application.home.usecase;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.home.dto.CategorySection;
import com.mininetflix.ministreaming.application.home.dto.HomeResponse;
import com.mininetflix.ministreaming.application.home.dto.VideoSummaryResponse;
import com.mininetflix.ministreaming.application.playback.port.PlaybackRepository;
import com.mininetflix.ministreaming.domain.content.VideoCategory;
import com.mininetflix.ministreaming.domain.content.VideoContent;
import com.mininetflix.ministreaming.domain.playback.PlaybackState;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetHomeUseCaseImpl implements GetHomeUseCase {

  private final VideoCatalogRepository videoRepository;
  private final PlaybackRepository playbackRepository;
  private final VideoStorageService videoStorageService;
  private final GetTrendingVideosUseCase trendingUseCase;

  @Override
  @Cacheable("home")
  public HomeResponse execute(String userId) {

    List<VideoSummaryResponse> trending = buildTrending();

    List<VideoSummaryResponse> continueWatching = buildContinueWatching(userId);

    List<CategorySection> categories = buildCategories();

    return new HomeResponse(trending, continueWatching, categories);
  }

  private List<VideoSummaryResponse> buildTrending() {

    return trendingUseCase.execute().stream()
        .map(
            video ->
                new VideoSummaryResponse(
                    video.videoId(),
                    video.title(),
                    video.thumbnailUrl(),
                    video.duration(),
                    video.category()))
        .toList();
  }

  private List<VideoSummaryResponse> buildContinueWatching(String userId) {

    return playbackRepository.findByUserAndNotCompleted(userId).stream()
        .sorted(Comparator.comparing(PlaybackState::getLastUpdated, Comparator.reverseOrder()))
        .limit(10)
        .map(
            playback ->
                videoRepository
                    .findById(playback.getContentId())
                    .filter(VideoContent::isActive)
                    .map(this::toSummary)
                    .orElse(null))
        .filter(Objects::nonNull)
        .toList();
  }

  private List<CategorySection> buildCategories() {

    return Arrays.stream(VideoCategory.values())
        .map(
            category -> {
              List<VideoSummaryResponse> videos =
                  videoRepository.findByCategory(category).stream()
                      .filter(VideoContent::isActive)
                      .limit(20)
                      .map(this::toSummary)
                      .toList();

              return videos.isEmpty() ? null : new CategorySection(category, videos);
            })
        .filter(Objects::nonNull)
        .toList();
  }

  private VideoSummaryResponse toSummary(VideoContent video) {

    String thumbnailUrl = null;

    if (video.getThumbnailKey() != null) {
      thumbnailUrl = videoStorageService.generatePublicUrl(video.getThumbnailKey());
    }

    return new VideoSummaryResponse(
        video.getId(), video.getTitle(), thumbnailUrl, video.getDuration(), video.getCategory());
  }
}

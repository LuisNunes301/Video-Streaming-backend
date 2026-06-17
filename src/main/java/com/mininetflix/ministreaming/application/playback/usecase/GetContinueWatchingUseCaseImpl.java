package com.mininetflix.ministreaming.application.playback.usecase;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.playback.dto.ContinueWatchingItem;
import com.mininetflix.ministreaming.application.playback.port.PlaybackRepository;
import com.mininetflix.ministreaming.domain.playback.PlaybackState;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetContinueWatchingUseCaseImpl implements GetContinueWatchingUseCase {

  private final PlaybackRepository playbackRepository;
  private final VideoCatalogRepository videoRepository;
  private final VideoStorageService storageService;

  @Override
  public List<ContinueWatchingItem> execute(String userId) {

    return playbackRepository.findByUserAndNotCompleted(userId).stream()
        .sorted(
            Comparator.comparing(
                PlaybackState::getLastUpdated, Comparator.nullsLast(Comparator.reverseOrder())))
        .limit(10)
        .map(
            playback -> {
              var video = videoRepository.findById(playback.getContentId()).orElse(null);

              if (video == null) {
                return null;
              }

              String thumbnailUrl = storageService.generatePublicUrl(video.getThumbnailKey());

              int progress = (int) ((playback.getCurrentTime() / video.getDuration()) * 100);

              return new ContinueWatchingItem(
                  video.getId(),
                  video.getTitle(),
                  thumbnailUrl,
                  video.getDuration(),
                  playback.getCurrentTime(),
                  progress,
                  video.getCategory());
            })
        .filter(Objects::nonNull)
        .toList();
  }
}

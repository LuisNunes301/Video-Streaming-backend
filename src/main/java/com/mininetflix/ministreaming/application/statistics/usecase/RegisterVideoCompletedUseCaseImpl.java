package com.mininetflix.ministreaming.application.statistics.usecase;

import com.mininetflix.ministreaming.application.statistics.port.VideoStatisticsRepository;
import com.mininetflix.ministreaming.domain.statistics.VideoStatistics;
import com.mininetflix.ministreaming.infrastructure.statistics.event.VideoCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterVideoCompletedUseCaseImpl implements RegisterVideoCompletedUseCase {

  private final VideoStatisticsRepository repository;

  @Override
  public void execute(VideoCompletedEvent event) {

    VideoStatistics statistics =
        repository.findByVideoId(event.videoId()).orElse(VideoStatistics.create(event.videoId()));

    statistics.registerCompletedView(event.watchedSeconds());

    repository.save(statistics);
  }
}

package com.mininetflix.ministreaming.application.statistics.port;

import com.mininetflix.ministreaming.domain.statistics.VideoStatistics;
import java.util.Optional;

public interface VideoStatisticsRepository {

  Optional<VideoStatistics> findByVideoId(String videoId);

  void save(VideoStatistics statistics);
}

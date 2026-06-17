package com.mininetflix.ministreaming.infrastructure.statistics.repository;

import com.mininetflix.ministreaming.infrastructure.statistics.entity.VideoStatisticsEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVideoStatisticsRepository extends JpaRepository<VideoStatisticsEntity, String> {
  List<VideoStatisticsEntity> findTop20ByOrderByViewsDescCompletedViewsDesc();
}

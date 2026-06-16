package com.mininetflix.ministreaming.infrastructure.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mininetflix.ministreaming.infrastructure.statistics.entity.VideoStatisticsEntity;

public interface JpaVideoStatisticsRepository extends JpaRepository<VideoStatisticsEntity, String> {
}

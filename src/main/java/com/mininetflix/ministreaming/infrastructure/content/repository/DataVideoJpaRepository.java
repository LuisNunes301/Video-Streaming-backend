package com.mininetflix.ministreaming.infrastructure.content.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mininetflix.ministreaming.domain.content.VideoCategory;

import com.mininetflix.ministreaming.infrastructure.content.entity.VideoEntity;

public interface DataVideoJpaRepository
                extends JpaRepository<VideoEntity, String> {
        List<VideoEntity> findByCategory(VideoCategory category);

        List<VideoEntity> findByIdIn(List<String> ids);

        List<VideoEntity> findByTitleContainingIgnoreCase(String title);
}
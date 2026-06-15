package com.mininetflix.ministreaming.infrastructure.playback.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mininetflix.ministreaming.infrastructure.content.entity.VideoEntity;
import com.mininetflix.ministreaming.infrastructure.playback.entity.PlaybackEntity;

public interface PlaybackJpaRepository
                extends JpaRepository<PlaybackEntity, UUID> {

        Optional<PlaybackEntity> findByUserIdAndContentId(
                        UUID userId,
                        UUID contentId);

        List<PlaybackEntity> findByUserIdAndCompletedFalse(UUID userId);

}
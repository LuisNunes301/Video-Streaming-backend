package com.mininetflix.ministreaming.infrastructure.playback.repository;

import com.mininetflix.ministreaming.infrastructure.playback.entity.PlaybackEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaybackJpaRepository extends JpaRepository<PlaybackEntity, UUID> {

  Optional<PlaybackEntity> findByUserIdAndContentId(UUID userId, UUID contentId);

  List<PlaybackEntity> findByUserIdAndCompletedFalse(UUID userId);
}

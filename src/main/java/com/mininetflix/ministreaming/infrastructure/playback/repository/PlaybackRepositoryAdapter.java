package com.mininetflix.ministreaming.infrastructure.playback.repository;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mininetflix.ministreaming.application.playback.port.PlaybackRepository;
import com.mininetflix.ministreaming.domain.playback.PlaybackState;
import com.mininetflix.ministreaming.infrastructure.playback.entity.PlaybackEntity;

@Repository
public class PlaybackRepositoryAdapter implements PlaybackRepository {

        private final PlaybackJpaRepository jpaRepository;

        public PlaybackRepositoryAdapter(PlaybackJpaRepository jpaRepository) {
                this.jpaRepository = jpaRepository;
        }

        @Override
        public Optional<PlaybackState> findByUserAndContent(
                        String userId,
                        String contentId) {

                return jpaRepository
                                .findByUserIdAndContentId(
                                                UUID.fromString(userId),
                                                UUID.fromString(contentId))
                                .map(this::toDomain);
        }

        @Override
        public List<PlaybackState> findByUserAndNotCompleted(String userId) {

                return jpaRepository
                                .findByUserIdAndCompletedFalse(UUID.fromString(userId))
                                .stream()
                                .map(this::toDomain)
                                .toList();
        }

        @Override
        public void save(PlaybackState state) {

                Optional<PlaybackEntity> existing = jpaRepository.findByUserIdAndContentId(
                                UUID.fromString(state.getUserId()),
                                UUID.fromString(state.getContentId()));

                PlaybackEntity entity = existing.orElse(new PlaybackEntity());

                if (entity.getId() == null) {
                        entity.setId(UUID.randomUUID());
                }

                entity.setUserId(UUID.fromString(state.getUserId()));
                entity.setContentId(UUID.fromString(state.getContentId()));
                entity.setCurrentTime(state.getCurrentTime());
                entity.setCompleted(state.isCompleted());
                entity.setUpdatedAt(
                                state.getLastUpdated() != null
                                                ? LocalDateTime.ofInstant(
                                                                state.getLastUpdated(),
                                                                ZoneId.systemDefault())
                                                : LocalDateTime.now());

                jpaRepository.save(entity);
        }

        private PlaybackState toDomain(PlaybackEntity entity) {

                PlaybackState state = new PlaybackState(
                                entity.getUserId().toString(),
                                entity.getContentId().toString());

                state.setCurrentTime(entity.getCurrentTime());
                state.setCompleted(entity.isCompleted());
                state.setLastUpdated(
                                entity.getUpdatedAt()
                                                .atZone(ZoneId.systemDefault())
                                                .toInstant());

                return state;
        }
}
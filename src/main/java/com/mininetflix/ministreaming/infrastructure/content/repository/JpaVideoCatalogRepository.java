package com.mininetflix.ministreaming.infrastructure.content.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.domain.content.VideoContent;
import com.mininetflix.ministreaming.infrastructure.content.entity.VideoEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaVideoCatalogRepository implements VideoCatalogRepository {

        private final DataVideoJpaRepository jpaRepository;

        @Override
        public void save(VideoContent video) {

                VideoEntity entity = VideoEntity.builder()
                                .id(video.getId())
                                .title(video.getTitle())
                                .objectKey(video.getObjectKey())
                                .status(video.getStatus())
                                .duration(video.getDuration())
                                .size(video.getSize())
                                .resolution(video.getResolution())
                                .thumbnailUrl(video.getThumbnailUrl())
                                .hlsPlaylistUrl(video.getHlsPlaylistUrl())
                                .processingError(video.getProcessingError())
                                .createdAt(video.getCreatedAt())
                                .processedAt(video.getProcessedAt())
                                .build();

                jpaRepository.save(entity);
        }

        @Override
        public Optional<VideoContent> findById(String id) {
                return jpaRepository.findById(id)
                                .map(this::toDomain);
        }

        @Override
        public List<VideoContent> findAll() {
                return jpaRepository.findAll()
                                .stream()
                                .map(this::toDomain)
                                .toList();
        }

        private VideoContent toDomain(VideoEntity entity) {

                VideoContent video = VideoContent.create(
                                entity.getId(),
                                entity.getTitle(),
                                entity.getObjectKey());

                // reconstrução do estado
                if (entity.getStatus() == null) {
                        return video;
                }

                switch (entity.getStatus()) {
                        case PROCESSING -> video.markProcessing();
                        case READY -> {
                                video.markProcessing();
                                video.markReady(
                                                entity.getDuration(),
                                                entity.getSize(),
                                                entity.getResolution(),
                                                entity.getThumbnailUrl(),
                                                entity.getHlsPlaylistUrl());
                        }
                        case FAILED -> {
                                video.markProcessing();
                                video.markFailed(entity.getProcessingError());
                        }
                        default -> {
                        }
                }

                return video;
        }
}
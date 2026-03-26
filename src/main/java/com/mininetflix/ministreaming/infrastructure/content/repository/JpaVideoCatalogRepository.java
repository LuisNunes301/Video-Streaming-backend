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
                                .height(video.getHeight())
                                .width(video.getWidth())
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

                return VideoContent.restore(
                                entity.getId(),
                                entity.getTitle(),
                                entity.getObjectKey(),
                                entity.getStatus(),
                                entity.getDuration(),
                                entity.getSize(),
                                entity.getWidth(),
                                entity.getHeight(),
                                entity.getThumbnailUrl(),
                                entity.getHlsPlaylistUrl(),
                                entity.getProcessingError(),
                                entity.getCreatedAt(),
                                entity.getProcessedAt());
        }
}
package com.mininetflix.ministreaming.infrastructure.content.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.domain.content.VideoCategory;
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
                                .category(video.getCategory())
                                .thumbnailKey(video.getThumbnailKey())
                                .hlsPlaylistKey(video.gethlsPlaylistKey())
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
                                entity.getCategory(),
                                entity.getThumbnailKey(),
                                entity.getHlsPlaylistKey(),
                                entity.getProcessingError(),
                                entity.getCreatedAt(),
                                entity.getProcessedAt());
        }

        @Override
        public List<VideoContent> findByCategory(VideoCategory category) {
                return jpaRepository.findByCategory(category)
                                .stream()
                                .map(this::toDomain)
                                .toList();
        }

        @Override
        public List<VideoContent> searchByTitle(String query) {

                return jpaRepository
                                .findByTitleContainingIgnoreCase(query)
                                .stream()
                                .map(this::toDomain)
                                .toList();
        }

}
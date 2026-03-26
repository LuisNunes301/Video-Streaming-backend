package com.mininetflix.ministreaming.application.content.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mininetflix.ministreaming.application.content.dto.UploadVideoInput;
import com.mininetflix.ministreaming.application.content.dto.UploadVideoOutput;
import com.mininetflix.ministreaming.application.content.port.DomainEventPublisher;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;

import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.domain.content.VideoContent;
import com.mininetflix.ministreaming.domain.content.event.VideoUploadedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadVideoUseCaseImpl implements UploadVideoUseCase {

        private final VideoStorageService storageService;
        private final VideoCatalogRepository catalogRepository;
        private final DomainEventPublisher eventPublisher;

        @Override
        @Transactional
        public UploadVideoOutput execute(UploadVideoInput input) {

                if (!"video/mp4".equals(input.file().getContentType())) {
                        throw new IllegalArgumentException("Only MP4 files are allowed");
                }

                String id = UUID.randomUUID().toString();
                String objectKey = id + "/original.mp4";

                storageService.upload(objectKey, input.file());

                VideoContent video = VideoContent.create(
                                id,
                                input.title(),
                                objectKey);

                catalogRepository.save(video);

                eventPublisher.publish(
                                new VideoUploadedEvent(
                                                video.getId(),
                                                video.getObjectKey()));

                return new UploadVideoOutput(
                                video.getId(),
                                video.getTitle(),
                                video.getObjectKey(), video.getStatus());
        }
}
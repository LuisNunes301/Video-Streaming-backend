package com.mininetflix.ministreaming.application.content.usecase;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoMetadataExtractor;

import com.mininetflix.ministreaming.domain.content.event.VideoUploadedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessVideoUseCaseImpl implements ProcessVideoUseCase {

    private final VideoMetadataExtractor metadataExtractor;
    private final VideoCatalogRepository repository;

    @Override
    public void execute(VideoUploadedEvent event) {

        var video = repository.findById(event.videoId())
                .orElseThrow(() -> new IllegalStateException("Video not found"));

        try {

            video.markProcessing();
            repository.save(video);

            var metadata = metadataExtractor.extract(
                    event.objectKey());

            video.markReady(
                    metadata.duration(),
                    metadata.size(),
                    metadata.resolution(),
                    null, // thumbnail virá depois
                    metadata.hlsPlaylistUrl());

            repository.save(video);

        } catch (Exception e) {

            video.markFailed(e.getMessage());
            repository.save(video);
        }
    }
}
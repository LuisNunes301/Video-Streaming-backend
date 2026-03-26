package com.mininetflix.ministreaming.application.content.usecase;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoMetadataExtractor;
import com.mininetflix.ministreaming.application.content.port.VideoPreviewGenerator;
import com.mininetflix.ministreaming.application.content.port.VideoTranscoder;
import com.mininetflix.ministreaming.domain.content.VideoStatus;
import com.mininetflix.ministreaming.domain.content.event.VideoUploadedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessVideoUseCaseImpl implements ProcessVideoUseCase {

    private final VideoMetadataExtractor metadataExtractor;
    private final VideoTranscoder videoTranscoder;
    private final VideoCatalogRepository repository;
    private final VideoPreviewGenerator previewGenerator;

    @Override
    public void execute(VideoUploadedEvent event) {

        System.out.println(">>> PROCESSANDO VIDEO: " + event.videoId());

        var video = repository.findById(event.videoId())
                .orElseThrow(() -> new IllegalStateException("Video not found"));

        if (video.getStatus() == VideoStatus.PROCESSING ||
                video.getStatus() == VideoStatus.READY) {

            System.out.println(">>> IGNORANDO: já processado ou em processamento");
            return;
        }

        video.markProcessing();
        repository.save(video);

        try {
            var metadata = metadataExtractor.extract(video.getObjectKey());

            String playlistKey = videoTranscoder.transcodeToHls(
                    video.getId(),
                    video.getObjectKey(),
                    metadata.width());

            var preview = previewGenerator.generate(
                    video.getId(),
                    video.getObjectKey());

            video.markReady(
                    metadata.duration(),
                    metadata.size(),
                    metadata.height(),
                    metadata.width(),
                    preview.thumbnailKey(),
                    playlistKey);

            repository.save(video);

        } catch (Exception e) {

            video.markFailed(e.getMessage());
            repository.save(video);

            throw e;
        }
    }
}
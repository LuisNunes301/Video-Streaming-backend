package com.mininetflix.ministreaming.application.playback.usecase;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.playback.dto.StartPlaybackOutput;
import com.mininetflix.ministreaming.application.playback.port.PlaybackRepository;
import com.mininetflix.ministreaming.domain.playback.PlaybackState;
import com.mininetflix.ministreaming.domain.playback.exception.VideoNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class StartPlaybackUseCaseImpl implements StartPlaybackUseCase {

        private final PlaybackRepository playbackRepository;
        private final VideoStorageService videoStorageService;
        private final VideoCatalogRepository videoCatalogRepository;

        public StartPlaybackUseCaseImpl(
                        PlaybackRepository playbackRepository,
                        VideoStorageService videoStorageService,
                        VideoCatalogRepository videoCatalogRepository) {
                this.playbackRepository = playbackRepository;
                this.videoStorageService = videoStorageService;
                this.videoCatalogRepository = videoCatalogRepository;
        }

        @Override
        public StartPlaybackOutput execute(String userId, String contentId) {

                var content = videoCatalogRepository.findById(contentId)
                                .orElseThrow(() -> new VideoNotFoundException(contentId));
                if (!content.isActive()) {
                        throw new IllegalStateException("Video is not available for playback");
                }

                double startAt = playbackRepository
                                .findByUserAndContent(userId, contentId)
                                .filter(state -> !state.isCompleted())
                                .map(PlaybackState::getCurrentTime)
                                .orElse(0.0);
                String videoUrl = videoStorageService
                                .generatePresignedUrl(content.getHlsPlaylistUrl());

                return new StartPlaybackOutput(videoUrl, startAt);

        }
}

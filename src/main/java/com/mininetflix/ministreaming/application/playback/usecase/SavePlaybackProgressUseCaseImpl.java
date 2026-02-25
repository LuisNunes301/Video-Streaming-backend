package com.mininetflix.ministreaming.application.playback.usecase;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.playback.dto.SavePlaybackProgressInput;
import com.mininetflix.ministreaming.application.playback.port.PlaybackRepository;
import com.mininetflix.ministreaming.domain.playback.PlaybackState;

@Service
public class SavePlaybackProgressUseCaseImpl
                implements SavePlaybackProgressUseCase {

        private final PlaybackRepository playbackRepository;
        private final VideoCatalogRepository videoCatalogRepository;

        public SavePlaybackProgressUseCaseImpl(
                        PlaybackRepository playbackRepository,
                        VideoCatalogRepository videoCatalogRepository) {
                this.playbackRepository = playbackRepository;
                this.videoCatalogRepository = videoCatalogRepository;
        }

        @Override
        public void execute(SavePlaybackProgressInput input) {

                var video = videoCatalogRepository
                                .findById(input.contentId())
                                .orElseThrow(() -> new IllegalArgumentException("Video not found"));

                double officialDuration = video.getDuration();

                PlaybackState state = playbackRepository
                                .findByUserAndContent(input.userId(), input.contentId())
                                .orElseGet(() -> new PlaybackState(
                                                input.userId(),
                                                input.contentId()));

                state.updateProgress(
                                input.currentTime(),
                                officialDuration);

                System.out.println("Official duration: " + officialDuration);
                System.out.println("New time: " + input.currentTime());
                System.out.println("Threshold: " + (officialDuration * 0.95));
                playbackRepository.save(state);
        }

}

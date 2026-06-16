package com.mininetflix.ministreaming.application.playback.usecase;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.playback.dto.SavePlaybackProgressInput;
import com.mininetflix.ministreaming.application.playback.port.PlaybackRepository;
import com.mininetflix.ministreaming.domain.playback.PlaybackState;
import com.mininetflix.ministreaming.infrastructure.statistics.event.VideoCompletedEvent;
import com.mininetflix.ministreaming.infrastructure.statistics.publisher.VideoCompletedPublisher;

@Service
public class SavePlaybackProgressUseCaseImpl
                implements SavePlaybackProgressUseCase {

        private final PlaybackRepository playbackRepository;
        private final VideoCatalogRepository videoCatalogRepository;
        private final VideoCompletedPublisher publisher;

        public SavePlaybackProgressUseCaseImpl(
                        PlaybackRepository playbackRepository,
                        VideoCatalogRepository videoCatalogRepository,
                        VideoCompletedPublisher publisher) {

                this.playbackRepository = playbackRepository;
                this.videoCatalogRepository = videoCatalogRepository;
                this.publisher = publisher;
        }

        @Override
        public void execute(
                        SavePlaybackProgressInput input) {

                var video = videoCatalogRepository
                                .findById(input.contentId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Video not found"));

                double officialDuration = video.getDuration();

                PlaybackState state = playbackRepository
                                .findByUserAndContent(
                                                input.userId(),
                                                input.contentId())
                                .orElseGet(() -> new PlaybackState(
                                                input.userId(),
                                                input.contentId()));

                state.updateProgress(
                                input.currentTime(),
                                officialDuration);

                if (state.isCompleted()
                                && !state.isCompletionRegistered()) {

                        publisher.publish(
                                        new VideoCompletedEvent(
                                                        input.userId(),
                                                        input.contentId(),
                                                        officialDuration));

                        state.setCompletionRegistered(true);
                }

                playbackRepository.save(state);
        }
}
package com.mininetflix.ministreaming.application.statistics.usecase;

import com.mininetflix.ministreaming.infrastructure.statistics.event.VideoCompletedEvent;

public interface RegisterVideoCompletedUseCase {

    void execute(
            VideoCompletedEvent event);
}

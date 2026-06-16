package com.mininetflix.ministreaming.infrastructure.statistics.publisher;

import com.mininetflix.ministreaming.infrastructure.statistics.event.VideoCompletedEvent;

public interface VideoCompletedPublisher {

    void publish(
            VideoCompletedEvent event);
}

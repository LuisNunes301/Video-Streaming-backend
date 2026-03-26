package com.mininetflix.ministreaming.infrastructure.messaging;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.mininetflix.ministreaming.application.content.port.DomainEventPublisher;
import com.mininetflix.ministreaming.domain.content.event.VideoUploadedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VideoUploadedTransactionalListener {

    private final DomainEventPublisher publisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(VideoUploadedEvent event) {
        publisher.publish(event);
    }
}
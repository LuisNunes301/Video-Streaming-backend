package com.mininetflix.ministreaming.infrastructure.content.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.application.content.usecase.ProcessVideoUseCaseImpl;
import com.mininetflix.ministreaming.domain.content.event.VideoUploadedEvent;
import com.mininetflix.ministreaming.infrastructure.messaging.RabbitConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VideoUploadedConsumer {

    private final ProcessVideoUseCaseImpl processVideoUseCase;

    @RabbitListener(queues = RabbitConfig.VIDEO_UPLOADED_QUEUE)
    public void handle(VideoUploadedEvent event) {
        processVideoUseCase.execute(event);
    }
}
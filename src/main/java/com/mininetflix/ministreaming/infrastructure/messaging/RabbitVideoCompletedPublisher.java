package com.mininetflix.ministreaming.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.infrastructure.statistics.event.VideoCompletedEvent;
import com.mininetflix.ministreaming.infrastructure.statistics.publisher.VideoCompletedPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RabbitVideoCompletedPublisher
        implements VideoCompletedPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(VideoCompletedEvent event) {

        System.out.println(
                "PUBLICANDO EVENTO VIDEO COMPLETED -> "
                        + event.videoId());

        rabbitTemplate.convertAndSend(
                RabbitConfig.VIDEO_EXCHANGE,
                RabbitConfig.VIDEO_COMPLETED_ROUTING_KEY,
                event);
    }
}
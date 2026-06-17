package com.mininetflix.ministreaming.infrastructure.messaging;

import com.mininetflix.ministreaming.application.content.port.DomainEventPublisher;
import com.mininetflix.ministreaming.domain.content.event.VideoUploadedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitDomainEventPublisher implements DomainEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(Object event) {

    if (event instanceof VideoUploadedEvent) {
      rabbitTemplate.convertAndSend(
          RabbitConfig.VIDEO_EXCHANGE, RabbitConfig.VIDEO_UPLOADED_ROUTING_KEY, event);
    }
  }
}

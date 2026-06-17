package com.mininetflix.ministreaming.infrastructure.statistics.consumer;

import com.mininetflix.ministreaming.application.statistics.usecase.RegisterVideoCompletedUseCase;
import com.mininetflix.ministreaming.infrastructure.messaging.RabbitConfig;
import com.mininetflix.ministreaming.infrastructure.statistics.event.VideoCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoCompletedConsumer {

  private final RegisterVideoCompletedUseCase useCase;

  @RabbitListener(queues = RabbitConfig.VIDEO_COMPLETED_QUEUE)
  public void consume(VideoCompletedEvent event) {

    useCase.execute(event);
  }
}

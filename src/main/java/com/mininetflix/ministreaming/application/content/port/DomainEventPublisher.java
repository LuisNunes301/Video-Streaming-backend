package com.mininetflix.ministreaming.application.content.port;

public interface DomainEventPublisher {

  void publish(Object event);
}

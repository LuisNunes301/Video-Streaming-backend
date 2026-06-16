package com.mininetflix.ministreaming.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /*
     * Exchange
     */
    public static final String VIDEO_EXCHANGE = "video.exchange";

    /*
     * Upload Processing
     */
    public static final String VIDEO_UPLOADED_QUEUE = "video.uploaded.queue";

    public static final String VIDEO_UPLOADED_ROUTING_KEY = "video.uploaded";

    /*
     * Statistics / Trending
     */
    public static final String VIDEO_COMPLETED_QUEUE = "video.completed.queue";

    public static final String VIDEO_COMPLETED_ROUTING_KEY = "video.completed";

    @Bean
    public TopicExchange videoExchange() {

        return new TopicExchange(
                VIDEO_EXCHANGE);
    }

    @Bean
    public Queue videoUploadedQueue() {

        return QueueBuilder
                .durable(VIDEO_UPLOADED_QUEUE)
                .build();
    }

    @Bean
    public Queue videoCompletedQueue() {

        return QueueBuilder
                .durable(VIDEO_COMPLETED_QUEUE)
                .build();
    }

    @Bean
    public Binding bindingVideoUploaded(
            Queue videoUploadedQueue,
            TopicExchange videoExchange) {

        return BindingBuilder
                .bind(videoUploadedQueue)
                .to(videoExchange)
                .with(VIDEO_UPLOADED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingVideoCompleted(
            Queue videoCompletedQueue,
            TopicExchange videoExchange) {

        return BindingBuilder
                .bind(videoCompletedQueue)
                .to(videoExchange)
                .with(VIDEO_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {

        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        template.setMessageConverter(converter);

        return template;
    }
}
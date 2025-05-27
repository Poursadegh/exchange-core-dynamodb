package exchange.core2.trading.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.orders}")
    private String ordersExchange;

    @Value("${rabbitmq.exchange.trades}")
    private String tradesExchange;

    @Value("${rabbitmq.queue.order-placed}")
    private String orderPlacedQueue;

    @Value("${rabbitmq.queue.order-cancelled}")
    private String orderCancelledQueue;

    @Value("${rabbitmq.queue.order-matched}")
    private String orderMatchedQueue;

    @Value("${rabbitmq.queue.trade-executed}")
    private String tradeExecutedQueue;

    @Value("${rabbitmq.routing-key.order-placed}")
    private String orderPlacedRoutingKey;

    @Value("${rabbitmq.routing-key.order-cancelled}")
    private String orderCancelledRoutingKey;

    @Value("${rabbitmq.routing-key.order-matched}")
    private String orderMatchedRoutingKey;

    @Value("${rabbitmq.routing-key.trade-executed}")
    private String tradeExecutedRoutingKey;

    @Bean
    public DirectExchange ordersExchange() {
        return new DirectExchange(ordersExchange);
    }

    @Bean
    public DirectExchange tradesExchange() {
        return new DirectExchange(tradesExchange);
    }

    @Bean
    public Queue orderPlacedQueue() {
        return QueueBuilder.durable(orderPlacedQueue)
                .withArgument("x-message-ttl", 60000)
                .build();
    }

    @Bean
    public Queue orderCancelledQueue() {
        return QueueBuilder.durable(orderCancelledQueue)
                .withArgument("x-message-ttl", 60000)
                .build();
    }

    @Bean
    public Queue orderMatchedQueue() {
        return QueueBuilder.durable(orderMatchedQueue)
                .withArgument("x-message-ttl", 60000)
                .build();
    }

    @Bean
    public Queue tradeExecutedQueue() {
        return QueueBuilder.durable(tradeExecutedQueue)
                .withArgument("x-message-ttl", 60000)
                .build();
    }

    @Bean
    public Binding orderPlacedBinding() {
        return BindingBuilder.bind(orderPlacedQueue())
                .to(ordersExchange())
                .with(orderPlacedRoutingKey);
    }

    @Bean
    public Binding orderCancelledBinding() {
        return BindingBuilder.bind(orderCancelledQueue())
                .to(ordersExchange())
                .with(orderCancelledRoutingKey);
    }

    @Bean
    public Binding orderMatchedBinding() {
        return BindingBuilder.bind(orderMatchedQueue())
                .to(ordersExchange())
                .with(orderMatchedRoutingKey);
    }

    @Bean
    public Binding tradeExecutedBinding() {
        return BindingBuilder.bind(tradeExecutedQueue())
                .to(tradesExchange())
                .with(tradeExecutedRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
} 
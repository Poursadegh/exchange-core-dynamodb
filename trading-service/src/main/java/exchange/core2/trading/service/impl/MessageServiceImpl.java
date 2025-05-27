package exchange.core2.trading.service.impl;

import exchange.core2.trading.dto.OrderResponse;
import exchange.core2.trading.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.orders}")
    private String ordersExchange;

    @Value("${rabbitmq.exchange.trades}")
    private String tradesExchange;

    @Value("${rabbitmq.routing-key.order-placed}")
    private String orderPlacedRoutingKey;

    @Value("${rabbitmq.routing-key.order-cancelled}")
    private String orderCancelledRoutingKey;

    @Value("${rabbitmq.routing-key.order-matched}")
    private String orderMatchedRoutingKey;

    @Value("${rabbitmq.routing-key.trade-executed}")
    private String tradeExecutedRoutingKey;

    @Override
    public Mono<Void> publishOrderPlaced(OrderResponse order) {
        return Mono.fromCallable(() -> {
            rabbitTemplate.convertAndSend(ordersExchange, orderPlacedRoutingKey, order);
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<Void> publishOrderCancelled(OrderResponse order) {
        return Mono.fromCallable(() -> {
            rabbitTemplate.convertAndSend(ordersExchange, orderCancelledRoutingKey, order);
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<Void> publishOrderMatched(OrderResponse order) {
        return Mono.fromCallable(() -> {
            rabbitTemplate.convertAndSend(ordersExchange, orderMatchedRoutingKey, order);
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<Void> publishTradeExecuted(OrderResponse order) {
        return Mono.fromCallable(() -> {
            rabbitTemplate.convertAndSend(tradesExchange, tradeExecutedRoutingKey, order);
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
} 
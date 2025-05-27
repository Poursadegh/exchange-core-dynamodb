package exchange.core2.trading.service;

import exchange.core2.trading.dto.OrderResponse;
import reactor.core.publisher.Mono;

public interface MessageService {
    Mono<Void> publishOrderPlaced(OrderResponse order);
    Mono<Void> publishOrderCancelled(OrderResponse order);
    Mono<Void> publishOrderMatched(OrderResponse order);
    Mono<Void> publishTradeExecuted(OrderResponse order);
} 
package exchange.core2.trading.service;

import exchange.core2.trading.dto.OrderRequest;
import exchange.core2.trading.dto.OrderResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderResponse> placeOrder(OrderRequest request);
    Mono<OrderResponse> getOrder(String orderId);
    Mono<OrderResponse> cancelOrder(String orderId);
    Flux<OrderResponse> getOrdersBySymbol(int symbolId);
    Flux<OrderResponse> getOrdersByUser(long userId);
} 
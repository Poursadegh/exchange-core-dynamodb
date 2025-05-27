package exchange.core2.trading.service.impl;

import exchange.core2.core.ExchangeCore;
import exchange.core2.core.common.OrderAction;
import exchange.core2.core.common.OrderType;
import exchange.core2.trading.dto.OrderRequest;
import exchange.core2.trading.dto.OrderResponse;
import exchange.core2.trading.exception.InvalidOrderException;
import exchange.core2.trading.exception.OrderNotFoundException;
import exchange.core2.trading.service.MessageService;
import exchange.core2.trading.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ExchangeCore exchangeCore;
    private final MessageService messageService;
    private final ReactiveRedisTemplate<String, OrderResponse> redisTemplate;
    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public Mono<OrderResponse> placeOrder(OrderRequest request) {
        return Mono.fromCallable(() -> {
            String orderId = UUID.randomUUID().toString();
            OrderResponse order = OrderResponse.builder()
                    .orderId(orderId)
                    .userId(request.getUserId())
                    .symbolId(request.getSymbolId())
                    .orderType(request.getOrderType())
                    .action(request.getAction())
                    .price(request.getPrice())
                    .size(request.getSize())
                    .status("ACTIVE")
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            // Submit order to exchange core using virtual thread
            return virtualThreadExecutor.submit(() -> {
                try {
                    // Place order in exchange core
                    // This is where you'd integrate with the actual exchange core
                    return order;
                } catch (Exception e) {
                    throw new InvalidOrderException("Failed to place order", e);
                }
            }).get();
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(order -> {
            // Cache the order
            return redisTemplate.opsForValue()
                    .set("order:" + order.getOrderId(), order, Duration.ofMinutes(10))
                    .thenReturn(order);
        })
        .flatMap(order -> {
            // Publish order placed event
            return messageService.publishOrderPlaced(order)
                    .thenReturn(order);
        });
    }

    @Override
    @Cacheable(value = "orders", key = "#orderId")
    public Mono<OrderResponse> getOrder(String orderId) {
        return redisTemplate.opsForValue()
                .get("order:" + orderId)
                .switchIfEmpty(Mono.defer(() -> {
                    // If not in cache, try to get from exchange core
                    return Mono.fromCallable(() -> {
                        // Retrieve order from core using virtual thread
                        return virtualThreadExecutor.submit(() -> {
                            // Get order from exchange core
                            // This is where you'd integrate with the actual exchange core
                            return null;
                        }).get();
                    })
                    .subscribeOn(Schedulers.boundedElastic())
                    .switchIfEmpty(Mono.error(new OrderNotFoundException(orderId)));
                }));
    }

    @Override
    @CacheEvict(value = "orders", key = "#orderId")
    public Mono<OrderResponse> cancelOrder(String orderId) {
        return getOrder(orderId)
                .flatMap(order -> Mono.fromCallable(() -> {
                    // Cancel order in exchange core using virtual thread
                    return virtualThreadExecutor.submit(() -> {
                        try {
                            // Cancel order in exchange core
                            order.setStatus("CANCELLED");
                            order.setUpdatedAt(Instant.now());
                            return order;
                        } catch (Exception e) {
                            throw new InvalidOrderException("Failed to cancel order", e);
                        }
                    }).get();
                })
                .subscribeOn(Schedulers.boundedElastic()))
                .flatMap(order -> {
                    // Update cache
                    return redisTemplate.opsForValue()
                            .set("order:" + orderId, order, Duration.ofMinutes(10))
                            .thenReturn(order);
                })
                .flatMap(order -> {
                    // Publish order cancelled event
                    return messageService.publishOrderCancelled(order)
                            .thenReturn(order);
                });
    }

    @Override
    public Flux<OrderResponse> getOrdersBySymbol(int symbolId) {
        return redisTemplate.keys("order:*")
                .flatMap(key -> redisTemplate.opsForValue().get(key))
                .filter(order -> order.getSymbolId() == symbolId)
                .switchIfEmpty(Flux.defer(() -> {
                    // If not in cache, get from exchange core
                    return Flux.fromCallable(() -> {
                        // Get orders from core using virtual thread
                        return virtualThreadExecutor.submit(() -> {
                            // Get orders from exchange core
                            // This is where you'd integrate with the actual exchange core
                            return null;
                        }).get();
                    })
                    .subscribeOn(Schedulers.boundedElastic());
                }));
    }

    @Override
    public Flux<OrderResponse> getOrdersByUser(long userId) {
        return redisTemplate.keys("order:*")
                .flatMap(key -> redisTemplate.opsForValue().get(key))
                .filter(order -> order.getUserId() == userId)
                .switchIfEmpty(Flux.defer(() -> {
                    // If not in cache, get from exchange core
                    return Flux.fromCallable(() -> {
                        // Get orders from core using virtual thread
                        return virtualThreadExecutor.submit(() -> {
                            // Get orders from exchange core
                            // This is where you'd integrate with the actual exchange core
                            return null;
                        }).get();
                    })
                    .subscribeOn(Schedulers.boundedElastic());
                }));
    }
} 
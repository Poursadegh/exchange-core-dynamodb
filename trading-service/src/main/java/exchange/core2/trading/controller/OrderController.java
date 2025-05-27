package exchange.core2.trading.controller;

import exchange.core2.core.common.OrderAction;
import exchange.core2.core.common.OrderType;
import exchange.core2.trading.dto.OrderRequest;
import exchange.core2.trading.dto.OrderResponse;
import exchange.core2.trading.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing trading orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place a new order")
    public Mono<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order details")
    public Mono<OrderResponse> getOrder(@PathVariable String orderId) {
        return orderService.getOrder(orderId);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Cancel an order")
    public Mono<OrderResponse> cancelOrder(@PathVariable String orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/symbol/{symbolId}")
    @Operation(summary = "Get orders for a symbol")
    public Flux<OrderResponse> getOrdersBySymbol(@PathVariable int symbolId) {
        return orderService.getOrdersBySymbol(symbolId);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders for a user")
    public Flux<OrderResponse> getOrdersByUser(@PathVariable long userId) {
        return orderService.getOrdersByUser(userId);
    }
} 
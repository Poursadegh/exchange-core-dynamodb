package exchange.core2.trading.exception;

import lombok.Getter;

@Getter
public class OrderNotFoundException extends RuntimeException {
    private final String orderId;

    public OrderNotFoundException(String orderId) {
        super(String.format("Order with ID %s not found", orderId));
        this.orderId = orderId;
    }
} 
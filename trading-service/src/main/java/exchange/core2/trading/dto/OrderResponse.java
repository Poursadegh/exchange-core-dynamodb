package exchange.core2.trading.dto;

import exchange.core2.core.common.OrderAction;
import exchange.core2.core.common.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "Order response")
public class OrderResponse {

    @Schema(description = "Order ID", example = "order-123")
    private String orderId;

    @Schema(description = "User ID", example = "12345")
    private Long userId;

    @Schema(description = "Symbol ID", example = "1")
    private Integer symbolId;

    @Schema(description = "Order type", example = "GTC")
    private OrderType orderType;

    @Schema(description = "Order action", example = "BID")
    private OrderAction action;

    @Schema(description = "Order price", example = "50000.00")
    private Double price;

    @Schema(description = "Order size", example = "1.5")
    private Double size;

    @Schema(description = "Filled size", example = "0.5")
    private Double filledSize;

    @Schema(description = "Order status", example = "ACTIVE")
    private String status;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    @Schema(description = "Last update timestamp")
    private Instant updatedAt;
} 
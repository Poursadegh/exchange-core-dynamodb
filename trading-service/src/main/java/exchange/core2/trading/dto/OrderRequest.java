package exchange.core2.trading.dto;

import exchange.core2.core.common.OrderAction;
import exchange.core2.core.common.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Order placement request")
public class OrderRequest {

    @NotNull
    @Schema(description = "User ID", example = "12345")
    private Long userId;

    @NotNull
    @Schema(description = "Symbol ID", example = "1")
    private Integer symbolId;

    @NotNull
    @Schema(description = "Order type", example = "GTC")
    private OrderType orderType;

    @NotNull
    @Schema(description = "Order action", example = "BID")
    private OrderAction action;

    @NotNull
    @Positive
    @Schema(description = "Order price", example = "50000.00")
    private Double price;

    @NotNull
    @Positive
    @Schema(description = "Order size", example = "1.5")
    private Double size;
} 
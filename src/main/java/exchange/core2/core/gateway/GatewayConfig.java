package exchange.core2.core.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("trading_route", r -> r
                        .path("/api/trading/**")
                        .filters(f -> f
                                .addRequestHeader("X-Request-Source", "gateway")
                                .stripPrefix(1))
                        .uri("http://localhost:8081"))
                .route("admin_route", r -> r
                        .path("/api/admin/**")
                        .filters(f -> f
                                .addRequestHeader("X-Request-Source", "gateway")
                                .stripPrefix(1))
                        .uri("http://localhost:8082"))
                .route("reporting_route", r -> r
                        .path("/api/reports/**")
                        .filters(f -> f
                                .addRequestHeader("X-Request-Source", "gateway")
                                .stripPrefix(1))
                        .uri("http://localhost:8083"))
                .build();
    }
} 
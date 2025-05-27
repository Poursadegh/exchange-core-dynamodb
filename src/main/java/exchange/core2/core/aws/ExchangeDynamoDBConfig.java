package exchange.core2.core.aws;

import exchange.core2.core.common.config.ExchangeConfiguration;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeDynamoDBConfig {
    private final String region;
    private final String endpoint;
    private final boolean enabled;
    private final boolean createTablesIfNotExist;
    private final ExchangeConfiguration exchangeConfig;

    public static ExchangeDynamoDBConfig defaultConfig(ExchangeConfiguration exchangeConfig) {
        return ExchangeDynamoDBConfig.builder()
                .region("us-east-1")
                .endpoint(null) // Use default AWS endpoint
                .enabled(true)
                .createTablesIfNotExist(true)
                .exchangeConfig(exchangeConfig)
                .build();
    }

    public DynamoDBConfig createDynamoDBConfig() {
        if (!enabled) {
            return null;
        }
        return endpoint != null ? 
            new DynamoDBConfig(region, endpoint) : 
            new DynamoDBConfig(region);
    }
} 
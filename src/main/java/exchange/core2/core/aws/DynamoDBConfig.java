package exchange.core2.core.aws;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbEnhancedClient;

import java.net.URI;

public class DynamoDBConfig {
    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient enhancedClient;

    public DynamoDBConfig(String region, String endpoint) {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    public DynamoDBConfig(String region) {
        this(region, "https://dynamodb." + region + ".amazonaws.com");
    }

    public DynamoDbClient getDynamoDbClient() {
        return dynamoDbClient;
    }

    public DynamoDbEnhancedClient getEnhancedClient() {
        return enhancedClient;
    }

    public void close() {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    }
} 
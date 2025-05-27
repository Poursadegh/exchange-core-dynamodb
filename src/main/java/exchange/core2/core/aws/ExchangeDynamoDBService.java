package exchange.core2.core.aws;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.paginators.ScanIterable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ExchangeDynamoDBService implements AutoCloseable {
    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient enhancedClient;
    private static final String ORDERS_TABLE = "exchange_orders";
    private static final String USERS_TABLE = "exchange_users";

    public ExchangeDynamoDBService(DynamoDBConfig config) {
        this.dynamoDbClient = config.getDynamoDbClient();
        this.enhancedClient = config.getEnhancedClient();
        createTablesIfNotExist();
    }

    private void createTablesIfNotExist() {
        createTableIfNotExists(ORDERS_TABLE, "orderId", "symbolId");
        createTableIfNotExists(USERS_TABLE, "userId", null);
    }

    private void createTableIfNotExists(String tableName, String partitionKey, String sortKey) {
        try {
            CreateTableRequest.Builder requestBuilder = CreateTableRequest.builder()
                    .tableName(tableName)
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName(partitionKey)
                            .attributeType(ScalarAttributeType.S)
                            .build());

            if (sortKey != null) {
                requestBuilder.attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(sortKey)
                        .attributeType(ScalarAttributeType.S)
                        .build());
            }

            KeySchemaElement partitionKeySchema = KeySchemaElement.builder()
                    .attributeName(partitionKey)
                    .keyType(KeyType.HASH)
                    .build();

            if (sortKey != null) {
                requestBuilder.keySchema(partitionKeySchema,
                        KeySchemaElement.builder()
                                .attributeName(sortKey)
                                .keyType(KeyType.RANGE)
                                .build());
            } else {
                requestBuilder.keySchema(partitionKeySchema);
            }

            dynamoDbClient.createTable(requestBuilder.build());
        } catch (ResourceInUseException e) {
            // Table already exists
        }
    }

    public CompletableFuture<Void> saveOrder(String orderId, String symbolId, Map<String, AttributeValue> orderData) {
        Map<String, AttributeValue> item = new HashMap<>(orderData);
        item.put("orderId", AttributeValue.builder().s(orderId).build());
        item.put("symbolId", AttributeValue.builder().s(symbolId).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(ORDERS_TABLE)
                .item(item)
                .build();

        return CompletableFuture.runAsync(() -> dynamoDbClient.putItem(request));
    }

    public CompletableFuture<Map<String, AttributeValue>> getOrder(String orderId, String symbolId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("orderId", AttributeValue.builder().s(orderId).build());
        key.put("symbolId", AttributeValue.builder().s(symbolId).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(ORDERS_TABLE)
                .key(key)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            GetItemResponse response = dynamoDbClient.getItem(request);
            return response.hasItem() ? response.item() : null;
        });
    }

    public CompletableFuture<Void> saveUser(String userId, Map<String, AttributeValue> userData) {
        Map<String, AttributeValue> item = new HashMap<>(userData);
        item.put("userId", AttributeValue.builder().s(userId).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(USERS_TABLE)
                .item(item)
                .build();

        return CompletableFuture.runAsync(() -> dynamoDbClient.putItem(request));
    }

    public CompletableFuture<Map<String, AttributeValue>> getUser(String userId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("userId", AttributeValue.builder().s(userId).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(USERS_TABLE)
                .key(key)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            GetItemResponse response = dynamoDbClient.getItem(request);
            return response.hasItem() ? response.item() : null;
        });
    }

    public ScanIterable scanOrders() {
        ScanRequest request = ScanRequest.builder()
                .tableName(ORDERS_TABLE)
                .build();
        return dynamoDbClient.scanPaginator(request);
    }

    public ScanIterable scanUsers() {
        ScanRequest request = ScanRequest.builder()
                .tableName(USERS_TABLE)
                .build();
        return dynamoDbClient.scanPaginator(request);
    }

    @Override
    public void close() {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    }
} 
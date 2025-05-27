package exchange.core2.core.aws;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeDynamoDBServiceTest {
    private static ExchangeDynamoDBService service;

    @BeforeAll
    public static void setup() {
        DynamoDBConfig config = new DynamoDBConfig("us-east-1", "http://localhost:8000");
        service = new ExchangeDynamoDBService(config);
    }

    @AfterAll
    public static void cleanup() {
        service.close();
    }

    @Test
    public void testSaveAndGetOrder() throws ExecutionException, InterruptedException {
        String orderId = "order1";
        String symbolId = "BTCUSD";
        Map<String, AttributeValue> orderData = new HashMap<>();
        orderData.put("price", AttributeValue.builder().n("1000").build());
        orderData.put("qty", AttributeValue.builder().n("10").build());
        service.saveOrder(orderId, symbolId, orderData).get();
        Map<String, AttributeValue> result = service.getOrder(orderId, symbolId).get();
        assertNotNull(result);
        assertEquals("1000", result.get("price").n());
        assertEquals("10", result.get("qty").n());
    }

    @Test
    public void testSaveAndGetUser() throws ExecutionException, InterruptedException {
        String userId = "user1";
        Map<String, AttributeValue> userData = new HashMap<>();
        userData.put("balance", AttributeValue.builder().n("5000").build());
        service.saveUser(userId, userData).get();
        Map<String, AttributeValue> result = service.getUser(userId).get();
        assertNotNull(result);
        assertEquals("5000", result.get("balance").n());
    }
} 
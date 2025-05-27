package exchange.core2.core.aws;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DynamoDBConfigTest {

    @Test
    public void testDynamoDBConfigCreation() {
        DynamoDBConfig config = new DynamoDBConfig("us-east-1", "http://localhost:8000");
        assertNotNull(config.getDynamoDbClient());
        assertNotNull(config.getEnhancedClient());
        config.close();
    }

    @Test
    public void testDynamoDBConfigDefaultEndpoint() {
        DynamoDBConfig config = new DynamoDBConfig("us-west-2");
        assertNotNull(config.getDynamoDbClient());
        assertNotNull(config.getEnhancedClient());
        config.close();
    }
} 
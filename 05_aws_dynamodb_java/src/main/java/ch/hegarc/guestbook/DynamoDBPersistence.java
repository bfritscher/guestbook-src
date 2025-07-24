package ch.hegarc.guestbook;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

public class DynamoDBPersistence {
    private static DynamoDbClient dynamoDbClient;
    private static DynamoDbEnhancedClient enhancedClient;

    public static DynamoDbClient getDynamoDbClient() {
        if (dynamoDbClient == null) {
            System.out.println("Creating DynamoDB client for region: us-east-1");
            dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_EAST_1) // Match Python implementation
                    .build();
        }
        return dynamoDbClient;
    }

    public static DynamoDbEnhancedClient getEnhancedClient() {
        if (enhancedClient == null) {
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(getDynamoDbClient())
                    .build();
        }
        return enhancedClient;
    }

    public static void setDynamoDbClient(DynamoDbClient client) {
        DynamoDBPersistence.dynamoDbClient = client;
        DynamoDBPersistence.enhancedClient = null; // Reset enhanced client
    }

    public static void setEnhancedClient(DynamoDbEnhancedClient client) {
        DynamoDBPersistence.enhancedClient = client;
    }
}

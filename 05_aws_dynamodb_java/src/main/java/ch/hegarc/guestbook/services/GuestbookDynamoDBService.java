package ch.hegarc.guestbook.services;

import ch.hegarc.guestbook.models.Entry;
import ch.hegarc.guestbook.models.DynamoDBEntry;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;

import static ch.hegarc.guestbook.DynamoDBPersistence.getEnhancedClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestbookDynamoDBService implements GuestbookService {

    private static final String TABLE_NAME = "guestbook";

    private DynamoDbTable<DynamoDBEntry> getTable() {
        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        DynamoDbTable<DynamoDBEntry> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoDBEntry.class));
        
        // Try to create the table if it doesn't exist
        try {
            table.describeTable();
        } catch (ResourceNotFoundException e) {
            // Table doesn't exist, create it with the same schema as Python version
            try {
                CreateTableEnhancedRequest.Builder requestBuilder = CreateTableEnhancedRequest.builder()
                        .provisionedThroughput(ProvisionedThroughput.builder()
                                .readCapacityUnits(1L)
                                .writeCapacityUnits(1L)
                                .build());
                
                table.createTable(requestBuilder.build());
                // Wait for table to be created
                table.describeTable();
                System.out.println("Table " + TABLE_NAME + " created successfully");
            } catch (Exception createException) {
                System.err.println("Error creating table: " + createException.getMessage());
            }
        }
        
        return table;
    }

    @Override
    public List<Entry> select() {
        System.out.println("DynamoDB select() called");
        List<Entry> entries = new ArrayList<>();
        try {
            System.out.println("Getting DynamoDB table...");
            DynamoDbTable<DynamoDBEntry> table = getTable();
            System.out.println("Table obtained, starting scan...");
            
            // Scan the table and collect all items avoiding ClassLoader issues
            List<DynamoDBEntry> dynamoEntries = new ArrayList<>();
            
            try {
                System.out.println("Executing DynamoDB scan...");
                // Use a more defensive approach to avoid ClassLoader issues
                table.scan(ScanEnhancedRequest.builder().build())
                        .items()
                        .forEach(item -> {
                            try {
                                System.out.println("Processing item: " + item.getEmail() + " - " + item.getDate());
                                // Create a fresh instance to avoid ClassLoader conflicts
                                DynamoDBEntry freshEntry = new DynamoDBEntry();
                                freshEntry.setEmail(item.getEmail());
                                freshEntry.setName(item.getName());
                                freshEntry.setMessage(item.getMessage());
                                freshEntry.setDate(item.getDate());
                                dynamoEntries.add(freshEntry);
                            } catch (Exception itemEx) {
                                System.err.println("Error processing item: " + itemEx.getMessage());
                                itemEx.printStackTrace();
                            }
                        });
                System.out.println("Scan completed. Found " + dynamoEntries.size() + " items");
            } catch (Exception scanEx) {
                System.err.println("Error during scan: " + scanEx.getMessage());
                scanEx.printStackTrace();
                return entries; // Return empty list
            }

            // Sort by date in ascending order (oldest first)
            dynamoEntries.sort((e1, e2) -> {
                try {
                    Instant date1 = Instant.parse(e1.getDate());
                    Instant date2 = Instant.parse(e2.getDate());
                    return date1.compareTo(date2); // Ascending order
                } catch (Exception ex) {
                    return e2.getDate().compareTo(e1.getDate()); // Fallback to string comparison
                }
            });
            
            // Convert to Entry objects
            entries = dynamoEntries.stream()
                    .map(DynamoDBEntry::toEntry)
                    .collect(Collectors.toList());
                    
        } catch (DynamoDbException e) {
            System.err.println("Error scanning DynamoDB table: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
        return entries;
    }

    @Override
    public void insert(Entry entry) {
        try {
            DynamoDbTable<DynamoDBEntry> table = getTable();
            DynamoDBEntry dynamoEntry = DynamoDBEntry.fromEntry(entry);
            table.putItem(dynamoEntry);
        } catch (DynamoDbException e) {
            System.err.println("Error inserting item to DynamoDB: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}

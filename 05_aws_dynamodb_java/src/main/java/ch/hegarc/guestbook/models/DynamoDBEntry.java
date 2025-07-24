package ch.hegarc.guestbook.models;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@DynamoDbBean
public class DynamoDBEntry {
    private String name;
    private String email;
    private String message;
    private String date;

    public DynamoDBEntry() {
        this.date = Instant.now().toString();
    }

    public DynamoDBEntry(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.date = Instant.now().toString();
    }

    public DynamoDBEntry(String name, String email, String message, Instant signedOn) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.date = signedOn != null ? signedOn.toString() : Instant.now().toString();
    }

    // Convert from Entry to DynamoDBEntry
    public static DynamoDBEntry fromEntry(Entry entry) {
        DynamoDBEntry dynamoEntry = new DynamoDBEntry();
        dynamoEntry.setName(entry.getName());
        dynamoEntry.setEmail(entry.getEmail());
        dynamoEntry.setMessage(entry.getMessage());
        dynamoEntry.setDate(entry.getSignedOn() != null ? entry.getSignedOn().toString() : Instant.now().toString());
        return dynamoEntry;
    }

    // Convert from DynamoDBEntry to Entry
    public Entry toEntry() {
        Instant signedOn;
        try {
            signedOn = Instant.parse(this.date);
        } catch (Exception e) {
            signedOn = Instant.now();
        }
        Entry entry = new Entry(this.name, this.email, this.message, signedOn);
        entry.setId(this.email + "#" + this.date); // Composite ID
        return entry;
    }

    @DynamoDbPartitionKey
    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDbSortKey
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSignedOnFormatted() {
        try {
            Instant instant = Instant.parse(this.date);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withLocale(Locale.UK)
                    .withZone(ZoneId.of("Europe/Zurich"));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    .withLocale(Locale.UK)
                    .withZone(ZoneId.of("Europe/Zurich"));
            return String.format("%s at %s", dateFormatter.format(instant), timeFormatter.format(instant));
        } catch (Exception e) {
            return this.date;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DynamoDBEntry)) return false;
        DynamoDBEntry that = (DynamoDBEntry) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(name, that.name) &&
                Objects.equals(message, that.message) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, message, date);
    }

    @Override
    public String toString() {
        return "DynamoDBEntry{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

package ch.hegarc.guestbook.models;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "entries")
public class Entry {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String email;
    private String message;
    private Instant signedOn;

    public Entry(){
        this.signedOn = Instant.now();
    }

    public Entry(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.signedOn = Instant.now();
    }

    public Entry(String name, String email, String message, Instant signedOn) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.signedOn = signedOn;
    }

    public String getId() {
        return id;
      }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSignedOn(Instant signedOn) {
        this.signedOn = signedOn;
    }
    public Instant getSignedOn() {
        return signedOn;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }


    public String getSignedOnFormatted() {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale( Locale.UK )
                .withZone( ZoneId.of("Europe/Zurich") );
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm")
                .withLocale( Locale.UK )
                .withZone( ZoneId.of("Europe/Zurich") );
        return String.format("%s at %s", date.format(signedOn), time.format(signedOn));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;
        Entry entry = (Entry) o;
        return name.equals(entry.name) &&
                email.equals(entry.email) &&
                message.equals(entry.message) &&
                signedOn.equals(entry.signedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, message, signedOn);
    }

    @Override
    public String toString() {
        return "Entry{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", signedOn=" + signedOn +
                '}';
    }
}

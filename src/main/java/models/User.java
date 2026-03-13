package models;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private String id;
    private String username;
    private String email;
    private LocalDateTime createdAt;

    // ───────────────────────────────────────────────
    //  1. Empty constructor (no-args)
    //     → must generate id + set createdAt
    // ───────────────────────────────────────────────
    public User() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    // ───────────────────────────────────────────────
    //  2. Constructor without id (most useful one)
    //     → generate id + set createdAt
    // ───────────────────────────────────────────────
    public User(String username, String email) {
        this();                               // calls no-args constructor → sets id & createdAt
        this.username = username;
        this.email = email;
    }

    // ───────────────────────────────────────────────
    //  3. Full constructor (used when loading from JSON)
    //     → does NOT generate id or createdAt
    // ───────────────────────────────────────────────
    public User(String id, String username, String email, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    // ───────────────────────────────────────────────
    //               Getters & Setters
    // ───────────────────────────────────────────────
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Optional: useful for debugging / logging
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
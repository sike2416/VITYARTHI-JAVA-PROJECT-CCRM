package edu.ccrm.domain;

import java.time.LocalDateTime;

public abstract class Person {
    private final String id;
    private final Name fullName;
    private String email;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

    public Person(String id, Name fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    // Abstract methods
    public abstract String getRole();
    public abstract String getProfileInfo();

    // Getters and setters
    public String getId() { return id; }
    public Name getFullName() { return fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Person{id='" + id + "', fullName=" + fullName +
                ", email='" + email + "', active=" + active + "}";
    }
}
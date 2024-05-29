package com.example.finalproject;

public class Notes {
    private int id;
    private String title;
    private String description;
    private long createdTime;

    public Notes() {
        // Default constructor required for SQLite
    }

    public Notes(String title, String description, long createdTime) {
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}

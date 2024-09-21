package com.example.ToDo.Model;

import java.io.Serializable;

public class ToDoModel implements Serializable {
    private int id;
    private String name;
    private String description;
    private String deadline;
    private String priority;
    private boolean status;

    // Constructor
    public ToDoModel(String name, String description, String deadline, String priority, boolean status) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        // Auto-generate ID
        this.id = generateAutoIncrementId();
    }

    // Getters and setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Method to generate auto-incrementing IDs
    private static int nextId = 1;

    private static int generateAutoIncrementId() {
        return nextId++;
    }
}

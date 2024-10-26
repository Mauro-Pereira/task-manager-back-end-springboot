package com.example.task_manager.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.task_manager.entity.entity_enum.STATUS;

import jakarta.persistence.Id;

@Document
public class Task {

    @Id
    private UUID taskId;
    private String title;
    private String description;
    private LocalDateTime expirationDate;
    private STATUS status;
    
    public Task(UUID taskId, String title, String description, LocalDateTime expirationDate, STATUS status) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.expirationDate = expirationDate;
        this.status = status;
    }
    public UUID getTaskId() {
        return taskId;
    }
    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
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
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
    public STATUS getStatus() {
        return status;
    }
    public void setStatus(STATUS status) {
        this.status = status;
    }


}

package com.example.task_manager.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import com.example.task_manager.entity.entity_enum.TASK_STATUS;

import jakarta.persistence.Id;

@Document
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime expirationDate;
    private TASK_STATUS status;
    
    public Task(String id, String title, String description, LocalDateTime expirationDate, TASK_STATUS status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.expirationDate = expirationDate;
        this.status = status;
    }

    public Task(){}

    public String getTaskId() {
        return id;
    }
    public void setTaskId(String taskId) {
        this.id = taskId;
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
    public TASK_STATUS getStatus() {
        return status;
    }
    public void setStatus(TASK_STATUS status) {
        this.status = status;
    }


}

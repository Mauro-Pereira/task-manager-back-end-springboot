package com.example.task_manager.entity;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.task_manager.entity.entity_enum.ROLE;

import jakarta.persistence.Id;

@Document
public class User {

    @Id
    private UUID userID;
    private String name;
    private String email;
    private String password;
    private boolean isAdmin = false;
    private ROLE role;
    private Set<Task> tasks;

    public User(UUID userID, String name, String email, String password, boolean isAdmin, ROLE role, Set<Task> tasks) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.role = role;
        this.tasks = tasks;
    }

    public User(){}

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
    
    

}

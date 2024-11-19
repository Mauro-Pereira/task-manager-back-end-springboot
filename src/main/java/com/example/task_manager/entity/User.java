package com.example.task_manager.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.task_manager.entity.entity_enum.ROLE;

import jakarta.persistence.Id;

@Document
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private boolean isAdmin = false;
    private ROLE role;
    private Set<String> tasks = new HashSet<>();

    public User(String id, String name, String email, String password, boolean isAdmin, ROLE role, Set<String> tasks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.role = role;
        this.tasks = tasks != null ? new HashSet<>(tasks) : new HashSet<>();
    }

    public User(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<String> getTasks() {
        return tasks;
    }

    public void setTasks(Set<String> tasks) {
        this.tasks = tasks;
    }
    
    

}

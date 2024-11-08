package com.example.task_manager.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.task_manager.entity.Task;

public interface TaskRepository extends MongoRepository<Task, String>{

    Optional<Task> findTaskById(String ID);
    void deleteTaskById(String ID);

}

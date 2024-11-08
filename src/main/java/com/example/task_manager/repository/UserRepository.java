package com.example.task_manager.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.task_manager.entity.User;

public interface UserRepository extends MongoRepository<User, String>{

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(String ID);
    void deleteUserById(String ID);

}

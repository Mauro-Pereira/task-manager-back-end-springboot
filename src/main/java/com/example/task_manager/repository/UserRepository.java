package com.example.task_manager.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.task_manager.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, UUID>{

}

package com.example.task_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.task_manager.entity.User;
import com.example.task_manager.entity.entity_enum.ROLE;
import com.example.task_manager.exception.UserAlreadyExistsException;
import com.example.task_manager.exception.UserNotFoundException;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public UserService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public User saveUser(User user){

        Optional<User> returnedUser = this.userRepository.findUserByEmail(user.getEmail());

        if(returnedUser.isPresent()){
            throw new UserAlreadyExistsException("User Already Exists");
        }

        user.setRole(user.isAdmin() ? ROLE.ADMIN: ROLE.USER);

        return this.userRepository.save(user);
        
    }

    public List<User> listUser(){
        return this.userRepository.findAll();
    }

    public User getUserById(String idUser){

        User returnedUser = this.userRepository.findUserById(idUser)
        .orElseThrow(() -> new UserNotFoundException("User not found"));;

        return returnedUser;
    }

    public void deleteUser(String userId){

        User user = this.userRepository.findUserById(userId) 
        .orElseThrow(() -> new UserNotFoundException("User not found")); 
        
        user.getTasks().forEach(taskId -> this.taskRepository.deleteTaskById(taskId)); 
        this.userRepository.deleteUserById(user.getId());

    }

    public User updateUser(String userId, User userRequest){

        User user = this.userRepository.findUserById(userId) 
        .orElseThrow(() -> new UserNotFoundException("User not found")); 
        
        if (userRequest != null) { 
            
            Optional.ofNullable(userRequest.getName()) 
            .filter(name -> !name.isEmpty()) 
            .ifPresent(user::setName); 
            
            Optional.ofNullable(userRequest.getEmail()) 
            .filter(email -> !email.isEmpty()) 
            .ifPresent(user::setEmail); 
            
            Optional.ofNullable(userRequest.getPassword()) 
            .filter(password -> !password.isEmpty()) 
            .ifPresent(user::setPassword); 
            
            if (userRequest.isAdmin() != user.isAdmin()) { 
                
                user.setRole(userRequest.isAdmin() ? ROLE.ADMIN : ROLE.USER); 
                user.setAdmin(userRequest.isAdmin()); 
            } 
        } 
        
        return this.userRepository.save(user);
    }


}

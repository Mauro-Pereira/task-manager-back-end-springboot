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

        if(user.isAdmin() == false){
            user.setRole(ROLE.USER);
        }else{
            user.setRole(ROLE.ADMIN);
        }

        return this.userRepository.save(user);
        
    }

    public List<User> listUser(){
        return this.userRepository.findAll();
    }

    public User getUserById(String idUser){

        Optional<User> returnedUser = this.userRepository.findUserById(idUser);

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        return returnedUser.get();
    }

    public void deleteUser(String idUser){

        Optional<User> returnedUser = this.userRepository.findUserById(idUser);

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        returnedUser.get().getTasks().forEach(taskId ->{
            this.taskRepository.deleteTaskById(taskId);
        });

        this.userRepository.deleteUserById(returnedUser.get().getId());

    }

    public User updateUser(String userId, User user){

        Optional<User> returnedUser = this.userRepository.findUserById(userId);

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        if(user != null && user.getName() != null){

            returnedUser.get().setName(user.getName());
        }

        if(user != null && user.getEmail() != null){

            returnedUser.get().setEmail(user.getEmail());

        }

        if(user != null && user.getPassword() != null){

            returnedUser.get().setPassword(user.getPassword());

        }

        if(user != null && user.isAdmin() != returnedUser.get().isAdmin()){

            if(user.isAdmin() == false){
                returnedUser.get().setRole(ROLE.USER);
            }else{
                returnedUser.get().setRole(ROLE.ADMIN);
            }

            returnedUser.get().setAdmin(user.isAdmin());

        }

        return this.userRepository.save(returnedUser.get());
    }


}

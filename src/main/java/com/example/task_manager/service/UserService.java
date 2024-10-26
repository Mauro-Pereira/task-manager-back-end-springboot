package com.example.task_manager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.task_manager.entity.User;
import com.example.task_manager.exception.UserAlreadyExistsException;
import com.example.task_manager.exception.UserNotFoundException;
import com.example.task_manager.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user){

        Optional<User> returnedUser = this.userRepository.findById(user.getUserID());

        if(returnedUser.isPresent()){
            throw new UserAlreadyExistsException("User Already Exists");
        }

        return this.userRepository.save(user);
        
    }

    public List<User> listUser(){
        return this.userRepository.findAll();
    }

    public User getUserById(UUID idUser){

        Optional<User> returnedUser = this.userRepository.findById(idUser);

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        return returnedUser.get();
    }

    public void deleteUser(UUID idUser){

        Optional<User> returnedUser = this.userRepository.findById(idUser);

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        this.userRepository.deleteById(returnedUser.get().getUserID());


    }

}

package com.example.task_manager.DTO;

import com.example.task_manager.entity.User;

public class Mapper {

    public static User userRequestToUser(UserRequest userRequest){

        User newUser = new User();
        newUser.setName(userRequest.getName());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(userRequest.getPassword());
        newUser.setAdmin(userRequest.isAdmin());
        return newUser;
        
    }

}

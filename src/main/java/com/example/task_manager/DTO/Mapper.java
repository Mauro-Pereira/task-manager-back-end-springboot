package com.example.task_manager.DTO;

import com.example.task_manager.entity.Task;
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

    public static UserResponse userToUserResponse(User user){
        UserResponse newUserResponse = new UserResponse();
        newUserResponse.setUserID(user.getUserID());
        newUserResponse.setName(user.getName());
        newUserResponse.setEmail(user.getEmail());
        newUserResponse.setAdmin(user.isAdmin());
        return newUserResponse;

    }

    public static Task taskResquestToTask(TaskRequest taskRequest){
        Task newTask = new Task();
        newTask.setTitle(taskRequest.getTitle());
        newTask.setDescription(taskRequest.getDescription());
        newTask.setExpirationDate(taskRequest.getExpirationDate());
        return newTask;
    }

}

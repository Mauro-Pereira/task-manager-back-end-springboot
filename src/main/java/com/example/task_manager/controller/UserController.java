package com.example.task_manager.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task_manager.DTO.Mapper;
import com.example.task_manager.DTO.UserRequest;
import com.example.task_manager.DTO.UserResponse;
import com.example.task_manager.entity.User;
import com.example.task_manager.exception.UserAlreadyExistsException;
import com.example.task_manager.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@RequestBody UserRequest userRequest) {
        try{
            return new ResponseEntity<>(userService.saveUser(Mapper.userRequestToUser(userRequest)), HttpStatus.OK);
        }catch(UserAlreadyExistsException e) {
            throw e;
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserRequest userResquest) {
        User updateUser = this.userService.updateUser(id, Mapper.userRequestToUser(userResquest));
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @GetMapping("/listAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> userResponseList = this.userService
                                            .listUser()
                                            .stream()
                                            .map(Mapper::userToUserResponse)
                                            .collect(Collectors.toList()
                                            );

        return new ResponseEntity<>(userResponseList, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return new ResponseEntity<>("Deleted with successfully", HttpStatus.OK);
    }

}

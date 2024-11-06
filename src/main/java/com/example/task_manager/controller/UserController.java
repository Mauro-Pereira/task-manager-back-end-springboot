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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Task Manager", description = "JWT Spring Boot API")
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @Operation(
        summary = "Register User",
        description = "A user must be registered in the system with their given name, email, password, and admin status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registered with successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Incorrect username or password"),
        @ApiResponse(responseCode = "404", description = "User Already Exists")
    })
    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@RequestBody UserRequest userRequest) {
        try{
            return new ResponseEntity<>(userService.saveUser(Mapper.userRequestToUser(userRequest)), HttpStatus.OK);
        }catch(UserAlreadyExistsException e) {
            throw e;
        }
    }

    @Operation(
        summary = "Update User",
        description = "A user must be updated in the system with their given name, email, password, and admin status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated with successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Incorrect username or password"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserRequest userResquest) {
        User updateUser = this.userService.updateUser(id, Mapper.userRequestToUser(userResquest));
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @Operation(
        summary = "List User",
        description = "A user must be listed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "If exists users in the system, it must be retorned"),
    })
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

    @Operation(
        summary = "Delete User",
        description = "Given an user id, this user must be deleted")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deleted with successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Incorrect username or password"),
        @ApiResponse(responseCode = "404", description = "User Not Found")
    })

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return new ResponseEntity<>("Deleted with successfully", HttpStatus.OK);
    }

}

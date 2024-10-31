package com.example.task_manager.controller;

import com.example.task_manager.service.TaskService;
import com.example.task_manager.service.UserService;

public class TaskController {

    private UserService userService;
    private TaskService taskService;

    public TaskController(UserService userService, TaskService taskService){
        this.userService = userService;
        this.taskService = taskService;
    }

    

}

package com.example.task_manager.controller;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
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
import com.example.task_manager.DTO.TaskRequest;
import com.example.task_manager.DTO.TaskResponse;
import com.example.task_manager.entity.Task;
import com.example.task_manager.exception.TaskAlreadyExistsException;
import com.example.task_manager.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;

@Tag(name = "Task Manager", description = "JWT Spring Boot API")
@RestController
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }


    @Operation(
        summary = "Register Task",
        description = "A task must be registered in the system with their given title, description, and expirationTime")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registered with successfully"),
        @ApiResponse(responseCode = "404", description = "Task Already Exists")
    })
    @PostMapping("/registerTask/{userId}")
    public ResponseEntity<?> registerTask(@RequestBody TaskRequest taskRequest, @PathVariable UUID userId) {
        try{
            return new ResponseEntity<>(taskService.addTask(Mapper.taskResquestToTask(taskRequest), userId), HttpStatus.OK);
        }catch(TaskAlreadyExistsException e) {
            throw e;
        }

    }


    @Operation(
        summary = "List All Tasks",
        description = "A Task must be listed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listed with successfully"),
        @ApiResponse(responseCode = "404", description = "Task Not Found")
    })
    @GetMapping("/listAllTask")
    public ResponseEntity<List<TaskResponse>> listAllTask() {
        List<TaskResponse> taskResponseList = this.taskService
                                    .listAllTask()
                                    .stream()
                                    .map(Mapper::taskToTaskResponse)
                                    .collect(Collectors.toList()
                                    );

        return new ResponseEntity<>(taskResponseList, HttpStatus.OK);
        }



        @Operation(
            summary = "List My Tasks",
            description = "A Task must be listed")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed with successfully"),
            @ApiResponse(responseCode = "404", description = "Task Not Found")
        })
        @GetMapping("/getMyTasks/{userId}")
        public ResponseEntity<List<TaskResponse>> getMyTask(@PathVariable UUID userId) {
            List<TaskResponse> taskResponseList = this.taskService
                                        .getMyTasks(userId)
                                        .stream()
                                        .map(Mapper::taskToTaskResponse)
                                        .collect(Collectors.toList()
                                        );
    
            return new ResponseEntity<>(taskResponseList, HttpStatus.OK);
        }


        @Operation(
            summary = "Delete Task",
            description = "Given an user id and task id, this user must be deleted")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted with successfully"),
            @ApiResponse(responseCode = "404", description = "Task Not Found")
        })
        @DeleteMapping("/deleteUser/{userId}/{taskId}")
        public ResponseEntity<?> deleteTask(@PathVariable UUID userId, @PathVariable UUID taskId) {
            taskService.deleteTask(userId, taskId);

            return new ResponseEntity<>("Deleted with successfully", HttpStatus.OK);
        }



        @Operation(
            summary = "Update Task",
            description = "Given an user id and task id, this user must be updated")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted with successfully"),
            @ApiResponse(responseCode = "404", description = "Task Not Found")
        })
        @PutMapping("/updateTask/{userid}/{taskId}")
        public ResponseEntity<Task> updateTask(@PathVariable UUID userId, @PathVariable UUID taskId, @RequestBody TaskRequest taskResquest) {
        Task updateTask = this.taskService.updateTask(userId, taskId, Mapper.taskResquestToTask(taskResquest));
        return new ResponseEntity<>(updateTask, HttpStatus.OK);
    }

    @Operation(
            summary = "Get Task Status",
            description = "return task status")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned with successfully"),
            @ApiResponse(responseCode = "404", description = "Task Not Found")
        })
    @GetMapping(value = "/task/updateStatus", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Task>> streamTaskUpdate(){
        return Flux.interval(Duration.ofSeconds(5))
                .map(interval -> taskService.listAllTask());
    }


    }

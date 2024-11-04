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

import com.example.task_manager.DTO.Mapper;
import com.example.task_manager.DTO.TaskRequest;
import com.example.task_manager.DTO.TaskResponse;
import com.example.task_manager.entity.Task;
import com.example.task_manager.exception.TaskAlreadyExistsException;
import com.example.task_manager.service.TaskService;

import reactor.core.publisher.Flux;

public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping("/registerTask/{userId}")
    public ResponseEntity<?> registerTask(@RequestBody TaskRequest taskRequest, @PathVariable UUID userId) {
        try{
            return new ResponseEntity<>(taskService.addTask(Mapper.taskResquestToTask(taskRequest), userId), HttpStatus.OK);
        }catch(TaskAlreadyExistsException e) {
            throw e;
        }

    }

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

        @DeleteMapping("/deleteUser/{userId}/{taskId}")
        public ResponseEntity<?> deleteTask(@PathVariable UUID userId, @PathVariable UUID taskId) {
            taskService.deleteTask(userId, taskId);

            return new ResponseEntity<>("Deleted with successfully", HttpStatus.OK);
        }

        @PutMapping("/updateTask/{userid}/{taskId}")
        public ResponseEntity<Task> updateTask(@PathVariable UUID userId, @PathVariable UUID taskId, @RequestBody TaskRequest taskResquest) {
        Task updateTask = this.taskService.updateTask(userId, taskId, Mapper.taskResquestToTask(taskResquest));
        return new ResponseEntity<>(updateTask, HttpStatus.OK);
    }

    @GetMapping(value = "/task/update", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Task>> streamTaskUpdate(){
        return Flux.interval(Duration.ofSeconds(5))
                .map(interval -> taskService.listAllTask());
    }


    }

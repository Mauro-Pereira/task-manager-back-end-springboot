package com.example.task_manager.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.entity.entity_enum.TASK_STATUS;
import com.example.task_manager.exception.TaskAlreadyExistsException;
import com.example.task_manager.exception.TaskNotFoundException;
import com.example.task_manager.exception.UserNotFoundException;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;

@Service
public class TaskService {

    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private List<Task> myOwnTasks;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository, List<Task> myOwnTasks) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.myOwnTasks = myOwnTasks;
    } 

    public Task addTask(Task task, String idUser){

        User returnedUser = this.userRepository.findUserById(idUser)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

        this.taskRepository.findTaskByTitle(task.getTitle())
        .orElseThrow(() -> new TaskAlreadyExistsException("Task Already exists"));

        Task savedTask = this.taskRepository.save(task);
        returnedUser.getTasks().add(savedTask.getTaskId());

        this.userRepository.save(returnedUser);

        return savedTask;
    }

    public List<Task> listAllTask(){

        List<Task> taskList = this.taskRepository.findAll();

        taskList.forEach(task ->{
            if(task.getExpirationDate().isBefore(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))){
                task.setStatus(TASK_STATUS.EXPIRED);
            }else{
                task.setStatus(TASK_STATUS.IN_TIME);
            }

            this.taskRepository.save(task);
        });


        return taskList;
    }

    public List<Task> getMyTasks(String userId){

        
        User returnedUser = this.userRepository.findUserById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

        returnedUser.getTasks().forEach(task ->{

            Optional<Task> returnedTask = this.taskRepository.findTaskById(task);

            if(returnedTask.isPresent()){
                if(returnedTask.get().getExpirationDate().isBefore(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))){
                    returnedTask.get().setStatus(TASK_STATUS.EXPIRED);
                }else{
                    returnedTask.get().setStatus(TASK_STATUS.IN_TIME);
                }

                this.taskRepository.save(returnedTask.get());
                this.myOwnTasks.add(returnedTask.get());
            }
        });

        return this.myOwnTasks;


    }

    public void deleteTask(String userId, String taskId){

        User returnedUser = this.userRepository.findUserById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

        this.taskRepository.findTaskById(taskId)
        .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        this.taskRepository.deleteTaskById(taskId);

        returnedUser.getTasks().remove(taskId); 

        this.userRepository.save(returnedUser);

    }

    public Task updateTask(String userId, String taskId, Task taskRequest) {

        this.userRepository.findUserById(userId) 
        .orElseThrow(() -> new UserNotFoundException("User not found")); 

        Task task = this.taskRepository.findTaskById(taskId) 
        .orElseThrow(() -> new TaskNotFoundException("Task not found")); 

        if (taskRequest != null) { 
            Optional.ofNullable(taskRequest.getTitle()) 
            .filter(title -> !title.isEmpty()) 
            .ifPresent(task::setTitle); 
            
            Optional.ofNullable(taskRequest.getDescription()) 
            .filter(description -> !description.isEmpty()) 
            .ifPresent(task::setDescription); 
            
            Optional.ofNullable(taskRequest.getExpirationDate()) 
            .ifPresent(task::setExpirationDate); } 
            
            this.taskRepository.save(task); 
            return task;
    }

}

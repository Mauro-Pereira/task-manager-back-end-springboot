package com.example.task_manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.task_manager.DTO.TaskRequest;
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

    public Task addTask(Task task, UUID idUser){

        
        Optional<User> returnedUser = this.userRepository.findById(idUser);
        Optional<Task> returnedTask = this.taskRepository.findById(task.getTaskId());

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        if(returnedTask.isPresent()){
            throw new TaskAlreadyExistsException("Task Already exists");
        }

        Task savedTask = this.taskRepository.save(task);
        returnedUser.get().getTasks().add(savedTask.getTaskId());

        return savedTask;
    }

    public List<Task> listAllTask(){

        List<Task> taskList = this.taskRepository.findAll();

        taskList.forEach(task ->{
            if(task.getExpirationDate().isBefore(LocalDateTime.now())){
                task.setStatus(TASK_STATUS.EXPIRED);
            }else{
                task.setStatus(TASK_STATUS.IN_TIME);
            }

            this.taskRepository.save(task);
        });


        return taskList;
    }

    public List<Task> getMyTasks(UUID userId){

        
        Optional<User> returnedUser = this.userRepository.findById(userId);

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        returnedUser.get().getTasks().forEach(task ->{

            Optional<Task> returnedTask = this.taskRepository.findById(task);

            if(returnedTask.isPresent()){
                if(returnedTask.get().getExpirationDate().isBefore(LocalDateTime.now())){
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

    public void deleteTask(UUID taskId, UUID userId){

        Optional<User> returnedUser = this.userRepository.findById(userId);
        Optional<Task> returnedTask = this.taskRepository.findById(taskId);

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        if(returnedTask.isEmpty()){
            throw new TaskNotFoundException("Task not found");
        }


        this.taskRepository.deleteById(taskId);


        returnedUser.get().getTasks().forEach(idTask ->{

            returnedUser.get().getTasks().removeIf(removedTask -> idTask == taskId); 

        });

    }

    public Task updateTask(UUID userId, UUID taskId, Task taskResquest) {
        Optional<User> returnedUser = this.userRepository.findById(userId);
        Optional<Task> returnedTask = this.taskRepository.findById(taskId);

        if(returnedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        if(returnedTask.isEmpty()){
            throw new TaskNotFoundException("Task not found");
        }


        if(taskResquest != null && taskResquest.getTitle() != null){

            returnedTask.get().setTitle(taskResquest.getTitle());
        }

        
        if(taskResquest != null && taskResquest.getDescription() != null){

            returnedTask.get().setDescription(taskResquest.getDescription());
        }

        
        if(taskResquest != null && taskResquest.getExpirationDate() != null){

            returnedTask.get().setExpirationDate(taskResquest.getExpirationDate());
        }

        return returnedTask.get();
    }

}

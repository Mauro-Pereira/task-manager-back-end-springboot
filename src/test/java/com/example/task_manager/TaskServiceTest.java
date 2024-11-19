package com.example.task_manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.entity.entity_enum.ROLE;
import com.example.task_manager.entity.entity_enum.TASK_STATUS;
import com.example.task_manager.exception.TaskAlreadyExistsException;
import com.example.task_manager.exception.TaskNotFoundException;
import com.example.task_manager.exception.UserNotFoundException;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.TaskService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private LocalDateTime dataTime;
    private Task task;
    private User user;

    @BeforeEach
    public void init() {
        this.dataTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        this.task = new Task("6733ac5eb2822c4054d18e3f", "Task Test", "This is a test", dataTime, TASK_STATUS.EXPIRED);
        this.user = new User("6733840f7389f61ad5276a55", "User", "User@email.com", "password", false, ROLE.USER, Set.of(task.getTaskId()));
    }

    @Test
    public void addTask_ShouldAddTask_WhenUserExistsAndTaskDoesNotExist() {
        String userId = user.getId();

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByTitle(task.getTitle())).thenReturn(Optional.empty());
        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.addTask(task, userId);

        assertEquals(task, savedTask);
        verify(userRepository).save(user);
        verify(taskRepository).save(task);
    }

    @Test
    public void addTask_ShouldThrowException_WhenTaskAlreadyExists() {
        String userId = user.getId();

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByTitle(task.getTitle())).thenReturn(Optional.of(task));

        assertThrows(TaskAlreadyExistsException.class, () -> taskService.addTask(task, userId));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void addTask_ShouldThrowException_WhenUserDoesNotExist() {
        String userId = "nonExistingUserId";

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskService.addTask(task, userId));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void listAllTask_ShouldUpdateTaskStatus_WhenTaskIsExpiredOrInTime() {
        Task expiredTask = new Task("task1", "Expired Task", "This task is expired", dataTime.minusDays(1), TASK_STATUS.EXPIRED);
        Task inTimeTask = new Task("task2", "In Time Task", "This task is in time", dataTime.plusDays(1), TASK_STATUS.IN_TIME);
        List<Task> tasks = List.of(expiredTask, inTimeTask);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> returnedTasks = taskService.listAllTask();

        assertEquals(TASK_STATUS.EXPIRED, returnedTasks.get(0).getStatus());
        assertEquals(TASK_STATUS.IN_TIME, returnedTasks.get(1).getStatus());
        verify(taskRepository, times(1)).save(expiredTask);
        verify(taskRepository, times(1)).save(inTimeTask);
    }

    @Test
    public void getMyTasks_ShouldReturnUserTasks_WhenUserExists() {
        String userId = user.getId();
        Task task1 = new Task("task1", "Task 1", "Description", dataTime.minusDays(1), TASK_STATUS.EXPIRED);
        Task task2 = new Task("task2", "Task 2", "Description", dataTime.plusDays(1), TASK_STATUS.IN_TIME);

        user.getTasks().add(task1.getTaskId());
        user.getTasks().add(task2.getTaskId());

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findTaskById("task1")).thenReturn(Optional.of(task1));
        when(taskRepository.findTaskById("task2")).thenReturn(Optional.of(task2));

        List<Task> myTasks = taskService.getMyTasks(userId);

        assertEquals(2, myTasks.size());
        assertEquals(TASK_STATUS.EXPIRED, myTasks.get(0).getStatus());
        assertEquals(TASK_STATUS.IN_TIME, myTasks.get(1).getStatus());
        verify(taskRepository, times(1)).save(task1);
        verify(taskRepository, times(1)).save(task2);
    }

    @Test
    public void deleteTask_ShouldRemoveTaskAndReference_WhenTaskAndUserExist() {
        String userId = user.getId();
        String taskId = task.getTaskId();

        user.getTasks().add(taskId);

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(userId, taskId);

        verify(taskRepository, times(1)).deleteTaskById(taskId);
        verify(userRepository, times(1)).save(user);
        assertFalse(user.getTasks().contains(taskId));
    }

    @Test
    public void deleteTask_ShouldThrowException_WhenUserNotFound() {
        String userId = "nonExistingUserId";
        String taskId = task.getTaskId();

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskService.deleteTask(userId, taskId));
        verify(taskRepository, never()).deleteTaskById(anyString());
    }

    @Test
    public void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        String userId = user.getId();
        String taskId = "nonExistingTaskId";

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(userId, taskId));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void updateTask_ShouldUpdateTaskFields_WhenUserAndTaskExist() {
        String userId = user.getId();
        String taskId = task.getTaskId();
        Task updatedTask = new Task(taskId, "Updated Task", "Updated Description", dataTime.plusDays(1), TASK_STATUS.IN_TIME);

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(userId, taskId, updatedTask);

        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(dataTime.plusDays(1), result.getExpirationDate());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void updateTask_ShouldThrowException_WhenUserNotFound() {
        String userId = "nonExistingUserId";
        String taskId = task.getTaskId();
        Task updatedTask = new Task(taskId, "Updated Task", "Updated Description", dataTime.plusDays(1), TASK_STATUS.IN_TIME);

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskService.updateTask(userId, taskId, updatedTask));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void updateTask_ShouldThrowException_WhenTaskNotFound() {
        String userId = user.getId();
        String taskId = "nonExistingTaskId";
        Task updatedTask = new Task(taskId, "Updated Task", "Updated Description", dataTime.plusDays(1), TASK_STATUS.IN_TIME);

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(userId, taskId, updatedTask));
        verify(taskRepository, never()).save(any(Task.class));
    }




}

package com.example.task_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.entity.entity_enum.ROLE;
import com.example.task_manager.entity.entity_enum.TASK_STATUS;
import com.example.task_manager.exception.UserAlreadyExistsException;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.TaskService;
import com.example.task_manager.service.UserService;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUserWhenItIsSave(){

        LocalDateTime dataTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        Set<String> tasks = new HashSet<>();
        Task task = new Task("6733ac5eb2822c4054d18e3f", "Task Test", "This is a test", dataTime, TASK_STATUS.EXPIRED);
        tasks.add(task.getTaskId());
        User user = new User("6733840f7389f61ad5276a55", "User", "User@email.com", "password", false, ROLE.USER, tasks);

        when(this.userRepository.save(user)).thenReturn(user);

        User returnedUser = this.userService.saveUser(user);

        assertEquals(user, returnedUser);
        assertEquals(user.getId(), returnedUser.getId());
        verify(this.userRepository, times(1)).save(user);

    }

    @Test
    void shouldReturnUserAlreadyExistsExceptionWhenItIsSave(){

        LocalDateTime dataTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        Set<String> tasks = new HashSet<>();
        Task task = new Task("6733ac5eb2822c4054d18e3f", "Task Test", "This is a test", dataTime, TASK_STATUS.EXPIRED);
        tasks.add(task.getTaskId());
        User user = new User("6733840f7389f61ad5276a55", "User", "User@email.com", "password", false, ROLE.USER, tasks);

        when(this.userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserAlreadyExistsException returnedException = assertThrows(
                UserAlreadyExistsException.class,
                () -> this.userService.saveUser(user)
            );

        assertEquals("User Already Exists", returnedException.getMessage());
        verify(this.userRepository, times(1)).findUserByEmail(user.getEmail());

    }

}

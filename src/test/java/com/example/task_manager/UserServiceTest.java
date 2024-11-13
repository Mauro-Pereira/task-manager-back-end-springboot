package com.example.task_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.example.task_manager.exception.UserAlreadyExistsException;
import com.example.task_manager.exception.UserNotFoundException;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserService userService;

    private LocalDateTime dataTime;
    private Set<String> tasks;
    private  Task task;
    private  User user;

    @BeforeEach
    public void init(){

        this.dataTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        this.task = new Task("6733ac5eb2822c4054d18e3f", "Task Test", "This is a test", dataTime, TASK_STATUS.EXPIRED);
        this.tasks = new HashSet<>();
        tasks.add(task.getTaskId());
        this.user = new User("6733840f7389f61ad5276a55", "User", "User@email.com", "password", false, ROLE.USER, tasks);

    }

    @Test
    void shouldReturnUserWhenItIsSave(){

        when(this.userRepository.save(user)).thenReturn(user);

        User returnedUser = this.userService.saveUser(user);

        assertEquals(user, returnedUser);
        assertEquals(user.getId(), returnedUser.getId());
        verify(this.userRepository, times(1)).save(user);

    }

    @Test
    void shouldReturnUserAlreadyExistsExceptionWhenItIsSave(){

        when(this.userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserAlreadyExistsException returnedException = assertThrows(
                UserAlreadyExistsException.class,
                () -> this.userService.saveUser(user)
            );

        assertEquals("User Already Exists", returnedException.getMessage());
        verify(this.userRepository, times(1)).findUserByEmail(user.getEmail());

    }

    @Test
    void shouldReturnAllUser(){
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        when(this.userRepository.findAll()).thenReturn(userList);

        List<User> returnedUserList = this.userRepository.findAll();

        assertEquals(returnedUserList, userList);
        assertEquals(returnedUserList.size(), userList.size());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserWhenUserIdIsPassaed(){
        String userId = "6733840f7389f61ad5276a55";
        when(this.userRepository.findUserById(userId)).thenReturn(Optional.of(user));

        Optional<User> returnedUser = this.userRepository.findById(userId);

        assertEquals(returnedUser.get(), user);
        assertEquals(userId, returnedUser.get().getId());
        verify(this.userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnUserNotFoundExceptionWhenUserNotExists(){

        String userId = "6733840f7389f61ad5276a55";
        when(this.userRepository.findUserById(userId)).thenReturn(Optional.of(user));

        UserNotFoundException returnedException = assertThrows(
            UserNotFoundException.class,
            () -> this.userService.getUserById(userId)
        );

        assertEquals("User not found", returnedException.getMessage());
        verify(this.userRepository, times(1)).findById(userId);

    }

}

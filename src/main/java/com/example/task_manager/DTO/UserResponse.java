package com.example.task_manager.DTO;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String userID;
    private String name;
    private String email;
    private boolean isAdmin;
    private Set<String> tasks = new HashSet<>();

}

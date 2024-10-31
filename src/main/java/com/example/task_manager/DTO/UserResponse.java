package com.example.task_manager.DTO;

import java.util.Set;
import java.util.UUID;


import com.example.task_manager.entity.entity_enum.ROLE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private UUID userID;
    private String name;
    private String email;
    private boolean isAdmin = false;
    private ROLE role;
    private Set<UUID> tasks;

}

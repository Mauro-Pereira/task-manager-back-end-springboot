package com.example.task_manager.DTO;

import java.util.Set;
import java.util.UUID;


import com.example.task_manager.entity.entity_enum.ROLE;

public class UserResponse {

    private UUID userID;
    private String name;
    private String email;
    private boolean isAdmin = false;
    private ROLE role;
    private Set<UUID> tasks;

}

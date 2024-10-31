package com.example.task_manager.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.task_manager.entity.entity_enum.TASK_STATUS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private UUID taskId;
    private String title;
    private String description;
    private LocalDateTime expirationDate;
    private TASK_STATUS status; 

}

package com.example.task_manager.exception;

public class TaskAlreadyExistsException extends RuntimeException{

    public TaskAlreadyExistsException(String message){
        super(message);
    }

}

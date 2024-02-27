package com.example.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends IllegalArgumentException{
    public UserNotFoundException(String message){
        log.error(message);
    }

}
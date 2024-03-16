package com.example.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingNotFoundException  extends IllegalArgumentException {
    public BookingNotFoundException (String message){
        log.error(message);
    }
}

package com.example.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService bookingService;
    @Autowired
    public BookingController() {

    }

}
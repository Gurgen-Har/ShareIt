package com.example.shareit.booking;

import com.example.shareit.booking.DTO.BookingDto;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStatus()
        );
    }
}

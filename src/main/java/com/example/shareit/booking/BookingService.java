package com.example.shareit.booking;

import com.example.shareit.booking.DTO.BookingDto;
import com.example.shareit.booking.DTO.BookingInputDto;
import com.example.shareit.booking.DTO.BookingShortDto;

import java.util.List;

public interface BookingService {
    BookingDto createQuery(BookingInputDto bookingInputDto, Long bookerId);
    BookingDto update(Long bookingId, Long userId, Boolean approved);
    BookingDto getBookingById(Long bookingId, Long userId);
    List<BookingDto> getBookingsByBooker(String state, Long userId);
    List<BookingDto> getBookingsByOwner(String state, Long userId);
    BookingShortDto getLastBooking(Long itemId);

    BookingShortDto getNextBooking(Long itemId);

    Booking getBookingWithUserBookedItem(Long itemId, Long userId);
}

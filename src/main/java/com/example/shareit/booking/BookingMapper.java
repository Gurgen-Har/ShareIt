package com.example.shareit.booking;

import com.example.shareit.Item.ItemMapper;
import com.example.shareit.Item.ItemServiceImpl;
import com.example.shareit.booking.DTO.BookingDto;
import com.example.shareit.booking.DTO.BookingInputDto;
import com.example.shareit.booking.DTO.BookingShortDto;
import com.example.shareit.user.UserMapper;
import com.example.shareit.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    private UserServiceImpl userService;
    private ItemServiceImpl itemService;
    private UserMapper userMapper;
    private ItemMapper itemMapper;
    @Autowired
    public BookingMapper(UserServiceImpl userService, ItemServiceImpl itemService,
                      UserMapper userMapper, ItemMapper itemMapper) {
        this.userService = userService;
        this.itemService = itemService;
        this.userMapper = userMapper;
        this.itemMapper = itemMapper;
    }


    public BookingDto toBookingDto(Booking booking) {
        if (booking != null) {
            return new BookingDto(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    itemMapper.toItemDto(booking.getItem()),
                    userMapper.toUserDto(booking.getBooker()),
                    booking.getStatus()
            );
        } else {
            return null;
        }
    }

    public BookingShortDto toBookingShortDto(Booking booking) {
        if (booking != null) {
            return new BookingShortDto(
                    booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
        } else {
            return null;
        }
    }

    public Booking toBooking(BookingInputDto bookingInputDto, Long bookerId) {
        return new Booking(
                null,
                bookingInputDto.getStart(),
                bookingInputDto.getEnd(),
                itemService.findItemById(bookingInputDto.getItemId()),
                userService.findUserById(bookerId),
                Status.WAITING
        );
    }
}

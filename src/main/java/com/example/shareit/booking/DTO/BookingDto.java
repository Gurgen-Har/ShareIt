package com.example.shareit.booking.DTO;

import com.example.shareit.Item.DTO.ItemDto;
import com.example.shareit.Item.Item;
import com.example.shareit.booking.Status;
import com.example.shareit.user.DTO.UserDto;
import com.example.shareit.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private Status status;
}

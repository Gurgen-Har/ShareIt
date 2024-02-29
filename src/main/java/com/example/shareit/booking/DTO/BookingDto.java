package com.example.shareit.booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class BookingDto {
    private LocalDate start;
    private LocalDate end;
    private Long itemId;
    private Long bookerId;
    private String status;
}

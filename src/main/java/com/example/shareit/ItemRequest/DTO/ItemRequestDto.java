package com.example.shareit.ItemRequest.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ItemRequestDto {
    private String description;
    private String requesterName;
    private LocalDateTime created;
}

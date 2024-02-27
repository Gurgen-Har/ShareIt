package com.example.shareit.ItemRequest;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private String requesterName;
    private LocalDateTime created;
}

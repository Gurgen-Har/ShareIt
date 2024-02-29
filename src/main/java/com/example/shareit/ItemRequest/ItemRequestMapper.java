package com.example.shareit.ItemRequest;

import com.example.shareit.ItemRequest.DTO.ItemRequestDto;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getDescription(),
                itemRequest.getRequesterName(),
                itemRequest.getCreated()
        );
    }
}

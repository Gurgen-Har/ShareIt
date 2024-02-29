package com.example.shareit.Item;

import com.example.shareit.Item.DTO.ItemDto;

public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getStatus(),
                item.getRequestId() != null ? item.getRequestId() : null
        );
    }

    public Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(
                itemDto.getId(),
                ownerId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getStatus(),
                itemDto.getRequestId() != null ? itemDto.getRequestId() : null
        );
    }
}

package com.example.shareit.Item;

import com.example.shareit.Item.DTO.ItemDto;
import com.example.shareit.service.CheckConsistencyService;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    private final CheckConsistencyService checker;

    @Autowired
    @Lazy
    public ItemMapper(CheckConsistencyService checkConsistencyService) {
        this.checker = checkConsistencyService;
    }

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId() != null ? item.getRequestId() : null,
                null,
                null,
                checker.getCommentsByItemId(item.getId()));
    }

    public ItemDto toItemExtDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId() != null ? item.getRequestId() : null,
                checker.getLastBooking(item.getId()),
                checker.getNextBooking(item.getId()),
                checker.getCommentsByItemId(item.getId()));
    }

    public Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                checker.findUserById(ownerId),
                itemDto.getRequestId() != null ? itemDto.getRequestId() : null
        );
    }

}

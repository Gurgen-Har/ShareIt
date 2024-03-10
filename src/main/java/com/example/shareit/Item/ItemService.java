package com.example.shareit.Item;

import com.example.shareit.Item.DTO.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsByOwner(Long ownerId);
    ItemDto create(ItemDto itemDto, Long ownerId);
    ItemDto update(ItemDto itemDto, Long ownerId, Long itemId);
    void delete(Long itemId, Long ownerId);
    Item findItemById(Long id);
    ItemDto getItemById(Long ownerId, Long id);
    List<ItemDto> getItemsBySearchQuery(String text);

}

package com.example.shareit.Item;

import com.example.shareit.Item.DTO.ItemDto;
import com.example.shareit.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl {
    private final ItemMapper itemMapper;
    private final ItemStorage itemStorage;
    @Autowired
    public ItemServiceImpl(@Qualifier("inMemoryItemStorage") ItemStorage itemStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.itemMapper = itemMapper;
    }
    public ItemDto create(ItemDto itemDto , Long ownerId) {
        return itemMapper.toItemDto(itemStorage.create(itemMapper.toItem(itemDto, ownerId)));
    }

    public ItemDto delete(Long id) {
        Item item = itemStorage.getItemById(id);
        if(!item.getOwnerId().equals(id)) {
            throw  new ItemNotFoundException("У пользователя нет данной вещи");
        }
        return itemMapper.toItemDto(itemStorage.delete(id));
    }

    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        Item oldItem = itemStorage.getItemById(itemId);
        if (!oldItem.getOwnerId().equals(ownerId)) {
            throw new ItemNotFoundException("У пользователя нет данной вещи");
        }
        return itemMapper.toItemDto(itemStorage.update(itemMapper.toItem(itemDto,ownerId)));
    }
    public void deleteItemsByOwner(Long ownerId) {
        itemStorage.deleteItemsByOwner(ownerId);
    }
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        return itemStorage.getItemsByOwner(ownerId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> getItemsBySearchQuery(String text) {
        text = text.toLowerCase();
        return itemStorage.getItemsBySearchQuery(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
    public ItemDto getItemsById(Long id) {
        return itemMapper.toItemDto(itemStorage.getItemById(id));
    }
}

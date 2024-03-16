package com.example.shareit.Item;

import com.example.shareit.Item.DTO.ItemDto;
import com.example.shareit.exception.ItemNotFoundException;
import com.example.shareit.exception.UserNotFoundException;
import com.example.shareit.user.UserRepository;
import com.example.shareit.user.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
    }
    @Override
    public ItemDto create(ItemDto itemDto , Long ownerId) {
        if (userRepository.findById(ownerId).isPresent()) {
            return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, ownerId)));
        } else {
            throw new UserNotFoundException("Пользователь с ID = " + ownerId + "не найден");
        }
    }

    @Override
    public void delete(Long itemId, Long ownerId) {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNotFoundException("У пользователя нет такой вещи"));
            if(!item.getOwner().getId().equals(ownerId)) {
                throw  new ItemNotFoundException("У пользователя нет данной вещи");
            }
            itemRepository.deleteById(itemId);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException("Вещ с ID = " + itemId + "не найдена");
        }
    }
    @Override
    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + ownerId + "не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещ с ID = " + itemId + "не найдена"));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemNotFoundException("У пользователя нет данной вещи");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        return itemMapper.toItemDto(itemRepository.save(item ));
    }

    @Override
    public Item findItemById(Long id) {
        if (id != null) {
            return itemRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Предмет с ID = " + id + "не найдена"));
        } else {
            throw new ItemNotFoundException("Передан пустой аргумент");
        }

    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + ownerId + "не найден"));
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ItemDto> getItemsBySearchQuery(String text) {
        if ((text != null) && (!text.isEmpty() && !text.isBlank())) {
            text = text.toLowerCase();
            return itemRepository.getItemBySearchQuery(text).stream()
                    .map(itemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
    @Override
    public ItemDto getItemById(Long ownerId, Long id) {
        ItemDto itemDto;
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Предмет с ID = " + id + "не найдена"));
        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + ownerId + "не найден"));
        if (ownerId.equals(item.getOwner().getId())) {
            itemDto = itemMapper.toItemExtDto(item);
        } else {
            itemDto = itemMapper.toItemDto(item);
        }
        return itemDto;
    }
}

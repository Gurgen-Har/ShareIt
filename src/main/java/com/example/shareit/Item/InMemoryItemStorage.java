package com.example.shareit.Item;

import com.example.shareit.exception.ItemNotFoundException;
import com.example.shareit.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("inMemoryItemStorage")
public class InMemoryItemStorage implements ItemStorage{
    private Map<Long, Item> items;
    private Long currentId;

    public InMemoryItemStorage() {
        items = new HashMap<>();
        currentId = 0L;
    }
    @Override
    public Item create(Item item) {
        if (isValidItem(item)) {
            item.setId(++currentId);
            items.put(item.getId(),item);
        }
        return item;
    }

    @Override
    public Item delete(Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой аргумент");
        }
        if (!items.containsKey(id)) {
            throw new ItemNotFoundException("Вещь с ID = " + id + "не найден");
        }
        return items.remove(id);
    }

    @Override
    public Item update(Item item) {
        if (item.getId() == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!items.containsKey(item.getId())) {
            throw new ItemNotFoundException("Вещь с ID = " + item.getId() + " не найдена!");
        }
        if (item.getName() == null) {
            item.setName(items.get(item.getId()).getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(items.get(item.getId()).getDescription());
        }
        if (item.getStatus() == null) {
            item.setStatus(items.get(item.getId()).getStatus());
        }
        if (isValidItem(item)) {
            items.put(item.getId(), item);
        }
        return item;
    }

    @Override
    public List<Item> getItemsByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsBySearchQuery(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (!text.isBlank()) {
            searchItems = items.values().stream()
                    .filter(Item::getStatus)
                    .filter(item -> item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                    .collect(Collectors.toList());
        }
        return searchItems;
    }

    @Override
    public void deleteItemsByOwner(Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!items.containsKey(ownerId)) {
            throw new ItemNotFoundException("Вещь с ID = " + ownerId + " не найдена!");
        }
        List<Long> deleteIds = items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .map(Item::getId)
                .collect(Collectors.toList());
        for (Long deleteId : deleteIds) {
            items.remove(deleteId);
        }
    }
    @Override
    public Item getItemById(Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!items.containsKey(id)) {
            throw new ItemNotFoundException("Вещь с ID = " + id + " не найдена!");
        }
        return items.get(id);
    }

    private boolean isValidItem(Item item) {
        if (item.getName().isEmpty() || item.getDescription().isEmpty() || item.getStatus() == null) {
            throw new ValidationException("У вещи некорректные данные");
        }
        return true;
    }
}

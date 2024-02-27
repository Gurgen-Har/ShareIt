package com.example.shareit.Item;

import org.springframework.stereotype.Component;

import java.util.List;
@Component("itemDao")

public class ItemDao implements ItemStorage{
    @Override
    public Item create(Item item) {
        return null;
    }

    @Override
    public Item delete(Long id) {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public List<Item> getItemsByOwner(Long ownerId) {
        return null;
    }

    @Override
    public List<Item> getItemsBySearchQuery(String text) {
        return null;
    }

    @Override
    public void deleteItemsByOwner(Long ownderId) {

    }

    @Override
    public Item getItemById(Long id) {
        return null;
    }
}

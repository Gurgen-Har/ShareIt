package com.example.shareit.Item;

import java.util.List;

public interface ItemStorage {
    public Item create(Item item);
    public Item delete(Long id);
    public Item update(Item item);
    List<Item> getItemsByOwner(Long ownerId);

    List<Item> getItemsBySearchQuery(String text);

    void deleteItemsByOwner(Long ownerId);
    public Item getItemById(Long id);
}

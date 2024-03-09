package com.example.shareit.Item;

import com.example.shareit.Item.DTO.ItemDto;
import com.example.shareit.user.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private static final String OWNER = "X-Sharer-User-Id";
    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl, UserServiceImpl userServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/{itemsId}")
    public ItemDto getItems(@PathVariable Long itemsId) {
        return itemServiceImpl.getItemsById(itemsId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(OWNER) Long ownerId) {
        return itemServiceImpl.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public  List<ItemDto> getItemsBySearchQuery(@RequestParam String text) {
        return itemServiceImpl.getItemsBySearchQuery(text);
    }

    @ResponseBody
    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader(OWNER) Long ownerId) {
        if (userServiceImpl.getUserById(ownerId) != null) {
            return itemServiceImpl.create(itemDto, ownerId);
        }
        return null;
    }


    @ResponseBody
    @PatchMapping("/{itemsId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader(OWNER) Long ownerId, @PathVariable Long itemsId) {
        if (userServiceImpl.getUserById(ownerId) != null && itemServiceImpl.getItemsById(itemsId) != null ) {
            return itemServiceImpl.update(itemDto, ownerId, itemsId);
        }
        return null;
    }

    @DeleteMapping("/{itemId}")
    public ItemDto deleteItemById(@PathVariable Long itemId) {
        return itemServiceImpl.delete(itemId);
    }

    @DeleteMapping
    public void deleteItemsByOwner(@RequestHeader(OWNER) Long ownerId) {
        itemServiceImpl.deleteItemsByOwner(ownerId);
    }
}

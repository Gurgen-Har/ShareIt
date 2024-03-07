package com.example.shareit.Item;

import com.example.shareit.Item.DTO.ItemDto;
import com.example.shareit.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private static final String OWNER = "X-Sharer-User-Id";
    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping("/{itemsId}")
    public ItemDto getItems(@PathVariable Long itemsId) {
        return itemService.getItemsById(itemsId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(OWNER) Long ownerId) {
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public  List<ItemDto> getItemsBySearchQuery(@RequestParam String text) {
        return itemService.getItemsBySearchQuery(text);
    }

    @ResponseBody
    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader(OWNER) Long ownerId) {
        if (userService.getUserById(ownerId) != null) {
            return itemService.create(itemDto, ownerId);
        }
        return null;
    }


    @ResponseBody
    @PatchMapping("/{itemsId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader(OWNER) Long ownerId, @PathVariable Long itemsId) {
        if (userService.getUserById(ownerId) != null && itemService.getItemsById(itemsId) != null ) {
            return itemService.update(itemDto, ownerId, itemsId);
        }
        return null;
    }

    @DeleteMapping("/{itemId}")
    public ItemDto deleteItemById(@PathVariable Long itemId) {
        return itemService.delete(itemId);
    }

    @DeleteMapping
    public void deleteItemsByOwner(@RequestHeader(OWNER) Long ownerId) {
        itemService.deleteItemsByOwner(ownerId);
    }
}

package com.example.shareit.Item;

import com.example.shareit.Item.DTO.ItemDto;
import com.example.shareit.user.UserService;
import com.example.shareit.user.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userServiceImpl;
    private static final String OWNER = "X-Sharer-User-Id";
    @Autowired
    public ItemController(ItemService itemService, UserService userServiceImpl) {
        this.itemService = itemService;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/{itemsId}")
    public ItemDto getItems(@PathVariable Long itemsId, @RequestHeader(OWNER) Long ownerId) {
        return itemService.getItemById(ownerId, itemsId);
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
        if (userServiceImpl.getUserById(ownerId) != null) {
            return itemService.create(itemDto, ownerId);
        }
        return null;
    }


    @ResponseBody
    @PatchMapping("/{itemsId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader(OWNER) Long ownerId, @PathVariable Long itemsId) {
        if (userServiceImpl.getUserById(ownerId) != null && itemService.findItemById(itemsId) != null ) {
            return itemService.update(itemDto, ownerId, itemsId);
        }
        return null;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@PathVariable Long itemId, @RequestHeader(OWNER) Long ownerId) {
        itemService.delete(itemId, ownerId);
    }

}

package com.example.shareit.service;

import com.example.shareit.Item.ItemService;
import com.example.shareit.Item.ItemServiceImpl;
import com.example.shareit.booking.Booking;
import com.example.shareit.booking.BookingService;
import com.example.shareit.booking.DTO.BookingShortDto;
import com.example.shareit.user.User;
import com.example.shareit.user.UserService;
import com.example.shareit.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckConsistencyService {
    private UserService userService;
    private ItemService itemService;
    private BookingService bookingService;

    @Autowired
    public CheckConsistencyService(UserServiceImpl userService, ItemServiceImpl itemService,
                                   BookingService bookingService) {
        this.userService = userService;
        this.itemService = itemService;
        this.bookingService = bookingService;
    }

    public boolean isExistUser(Long userId) {
        boolean exist = false;
        if (userService.getUserById(userId) != null) {
            exist = true;
        }
        return exist;
    }

    public boolean isAvailableItem(Long itemId) {
        return itemService.findItemById(itemId).getAvailable();
    }

    public boolean isItemOwner(Long itemId, Long userId) {

        return itemService.getItemsByOwner(userId).stream()
                .anyMatch(i -> i.getId().equals(itemId));
    }

    public User findUserById(Long userId) {
        return userService.findUserById(userId);
    }

    public BookingShortDto getLastBooking(Long itemId) {
        return bookingService.getLastBooking(itemId);
    }

    public BookingShortDto getNextBooking(Long itemId) {
        return bookingService.getNextBooking(itemId);
    }

    public Booking getBookingWithUserBookedItem(Long itemId, Long userId) {
        return bookingService.getBookingWithUserBookedItem(itemId, userId);
    }

    public List<CommentDto> getCommentsByItemId(Long itemId) {
        return itemService.getCommentsByItemId(itemId);
    }
}

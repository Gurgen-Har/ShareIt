package com.example.shareit.user;

import org.springframework.stereotype.Component;

import java.util.List;
@Component("userDao")

public class UserDao implements UserStorage{
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User delete(Long id) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return null;
    }
}

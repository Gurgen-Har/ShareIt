package com.example.shareit.user;

import java.util.List;

public interface UserStorage {
    public User create(User user);
    public User delete(Long id);
    public User update(User user);
    List<User> getUsers();
    User getUserById(Long id);
}

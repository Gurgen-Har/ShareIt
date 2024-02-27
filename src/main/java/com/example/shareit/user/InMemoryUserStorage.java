package com.example.shareit.user;

import com.example.shareit.exception.UserAlreadyExistsException;
import com.example.shareit.exception.UserNotFoundException;
import com.example.shareit.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryUserStorage")

public class InMemoryUserStorage implements UserStorage {
    Map<Long,User> users;
    Long currentId;
    public InMemoryUserStorage() {
        users = new HashMap<>();
        currentId = 0L;
    }
    @Override
    public User create(User user) {
        if (isValidUser(user)) {
            if (user.getId() == null) {
                user.setId(++currentId);
            }
            users.put(user.getId(), user);

        } else {
            throw new UserAlreadyExistsException("Пользователь с E-mail=" + user.getEmail() + " уже существует!");
        }
        return user;
    }

    @Override
    public User delete(Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой аргумент");
        }
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID (" + id + ") не найден");
        }
        return users.remove(id);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Передан пустой аргумент");
        }
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь с ID (" + user.getId() + ") не найден");
        }
        if (user.getName() == null) {
            user.setName(users.get(user.getId()).getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(users.get(user.getId()).getEmail());
        }
        if (users.values().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .allMatch(u -> u.getId().equals(user.getId()))) {
            if (isValidUser(user)) {
                users.put(user.getId(), user);
            }
        } else {
            throw new UserAlreadyExistsException("Пользователь с E-mail=" + user.getEmail() + " уже существует!");
        }

        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID (" + id + ") не найден");
        }
        return users.get(id);
    }

    private boolean isValidUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if ((user.getName().isEmpty()) || (user.getName().contains(" "))) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getName());
        }
        return true;
    }
}

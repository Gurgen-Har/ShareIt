package com.example.shareit.user;

import com.example.shareit.user.DTO.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto create(UserDto userDto);
    UserDto getUserById(Long id);
    UserDto update(UserDto userDto, Long id);
    void delete(Long id);
    User findUserById(Long id);
}

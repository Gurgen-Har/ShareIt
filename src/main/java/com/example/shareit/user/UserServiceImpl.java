package com.example.shareit.user;

import com.example.shareit.exception.UserAlreadyExistsException;
import com.example.shareit.exception.UserNotFoundException;
import com.example.shareit.user.DTO.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
    @Override
    public UserDto create(UserDto userDto) {
        try {
            return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("Пользователь с E-mail=" +
                    userDto.getEmail() + " уже существует!");
        }
    }
    @Override
    public UserDto update(UserDto userDto, Long id) {
        if (userDto.getId() == null) {
            userDto.setId(id);
        }
        User user  = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + id + " не найден"));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(userDto.getEmail())
                    .stream()
                    .filter(u ->u.getEmail().equals(userDto.getEmail()))
                    .allMatch(u -> u.getId().equals(userDto.getId()))) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new UserAlreadyExistsException("Пользователь с E-mail" + user.getEmail() + "уже существует");
            }
        }

        return userMapper.toUserDto(userRepository.save(user));
    }
    @Override
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с ID = " + id + "не найден");
        }
    }
    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(
                () -> new UserAlreadyExistsException("Пользователь с ID = " + id +"не найден")));
    }
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + id + "не найден"));
    }

}

package com.example.shareit.user;

import com.example.shareit.user.DTO.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl){
        this.userServiceImpl = userServiceImpl;
    }
    @GetMapping
    public List<UserDto> getUsers(){
        return userServiceImpl.getUsers();
    }
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userServiceImpl.getUserById(id);
    }

    @ResponseBody
    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userServiceImpl.create(userDto);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public UserDto updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Long id ) {
        return userServiceImpl.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id){
        return userServiceImpl.delete(id);
    }


}

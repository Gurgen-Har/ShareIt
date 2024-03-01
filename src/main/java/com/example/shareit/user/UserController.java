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
    UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @ResponseBody
    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public UserDto updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Long id ) {
        return userService.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id){
        return userService.delete(id);
    }


}

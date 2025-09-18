package ru.bulgakov.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bulgakov.spring.dto.user.rq.UserDtoRq;
import ru.bulgakov.spring.dto.user.rq.UserUpdateDtoRq;
import ru.bulgakov.spring.dto.user.rs.UserDtoRs;
import ru.bulgakov.spring.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDtoRs> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDtoRs getUserById(@PathVariable("userId") long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDtoRs createUser(@RequestBody UserDtoRq dto) {
        return userService.createUser(dto.username());
    }

    @PutMapping
    public UserDtoRs updateUser(@RequestBody UserUpdateDtoRq dto) {
        return userService.updateUser(dto.id(), dto.username());
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") long id) {
        userService.deleteUser(id);
    }
}

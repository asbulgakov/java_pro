package ru.bulgakov.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.bulgakov.spring.converter.UserConverter;
import ru.bulgakov.spring.service.UserService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class UserCommands {

    private final UserService userService;

    private final UserConverter userConverter;

    // au - all users
    @ShellMethod(value = "Find all users", key = "au")
    public String findAllUsers() {
        return userService.getAllUsers().stream()
                .map(userConverter::userToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // ubid 1
    @ShellMethod(value = "Find user by id", key = "ubid")
    public String findUserById(long id) {
        return userService.getUserById(id)
                .map(userConverter::userToString)
                .orElse("User with id %d not found".formatted(id));
    }

    // ucrt new_user_name
    @ShellMethod(value = "Create user", key = "ucrt")
    public String saveUser(String username) {
        var savedUser = userService.createUser(username);
        return userConverter.userToString(savedUser);
    }

    // uupd 4 name_to_update
    @ShellMethod(value = "Update user", key = "uupd")
    public String updateUser(long id, String username) {
        var savedUser = userService.updateUser(id, username);
        return userConverter.userToString(savedUser);
    }

    // udel 1
    @ShellMethod(value = "Delete user by id", key = "udel")
    public void deleteUser(long id) {
        userService.deleteUser(id);
    }
}

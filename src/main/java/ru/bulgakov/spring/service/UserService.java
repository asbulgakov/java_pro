package ru.bulgakov.spring.service;

import ru.bulgakov.spring.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User createUser(String username);
    User updateUser(Long id, String username);
    boolean deleteUser(Long id);
}

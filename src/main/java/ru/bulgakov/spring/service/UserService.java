package ru.bulgakov.spring.service;

import ru.bulgakov.spring.dto.user.rs.UserDtoRs;

import java.util.List;

public interface UserService {
    UserDtoRs getUserById(Long id);
    List<UserDtoRs> getAllUsers();
    UserDtoRs createUser(String username);
    UserDtoRs updateUser(Long id, String username);
    void deleteUser(Long id);
}

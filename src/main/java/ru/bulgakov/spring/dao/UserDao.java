package ru.bulgakov.spring.dao;

import ru.bulgakov.spring.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(Long id);
    List<User> findAll();
    User create(User user);
    User update(User user);
    boolean deleteById(Long id);
}

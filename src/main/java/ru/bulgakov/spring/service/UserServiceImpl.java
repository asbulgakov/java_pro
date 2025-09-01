package ru.bulgakov.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bulgakov.spring.dao.UserDao;
import ru.bulgakov.spring.exception.NotFoundUserException;
import ru.bulgakov.spring.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User createUser(String username) {
        validateUserName(username);

        User user = new User();
        user.setUsername(username.trim());
        return userDao.create(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        validateUserId(id);

        return userDao.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User updateUser(Long id, String username) {
        validateUserId(id);
        validateUserName(username);

        Optional<User> existingUser = userDao.findById(id);
        if (existingUser.isEmpty()) {
            throw new NotFoundUserException("User not found with id: " + id);
        }

        User user = existingUser.get();
        user.setUsername(username.trim());
        return userDao.update(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        validateUserId(id);

        return userDao.deleteById(id);
    }

    private void validateUserId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
    }

    private void validateUserName(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
    }
}
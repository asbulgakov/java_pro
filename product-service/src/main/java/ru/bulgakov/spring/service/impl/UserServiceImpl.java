package ru.bulgakov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bulgakov.spring.dto.user.rs.UserDtoRs;
import ru.bulgakov.spring.exception.UserDeletedException;
import ru.bulgakov.spring.exception.UserNotFoundException;
import ru.bulgakov.spring.exception.UserAlreadyExistsException;
import ru.bulgakov.spring.mapper.UserMapper;
import ru.bulgakov.spring.model.Product;
import ru.bulgakov.spring.model.User;
import ru.bulgakov.spring.repository.ProductRepository;
import ru.bulgakov.spring.repository.UserRepository;
import ru.bulgakov.spring.service.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDtoRs createUser(String username) {
        log.info("Creating user with username: {}", username);

        String trimmedUsername = username.trim();
        validateUsername(trimmedUsername);

        if (userRepository.existsByUsername(trimmedUsername)) {
            throw new UserAlreadyExistsException("User with username '" + trimmedUsername + "' already exists");
        }

        User user = new User();
        user.setUsername(trimmedUsername);
        User savedUser = userRepository.save(user);

        log.info("User created successfully with id: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDtoRs getUserById(Long id) {
        log.info("Getting user by id: {}", id);
        validateUserId(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDtoRs> getAllUsers() {
        log.info("Getting all users");
        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDtoRs updateUser(Long id, String username) {
        log.info("Updating user with id: {} and username: {}", id, username);

        validateUserId(id);
        String trimmedUsername = username.trim();
        validateUsername(trimmedUsername);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.findByUsername(trimmedUsername)
                .ifPresent(duplicateUser -> {
                    if (!duplicateUser.getId().equals(id)) {
                        throw new UserAlreadyExistsException("User with username '" + trimmedUsername + "' already exists");
                    }
                });

        user.setUsername(trimmedUsername);
        User updatedUser = userRepository.save(user);

        log.info("User updated successfully with id: {}", updatedUser.getId());
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        validateUserId(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });

        // Проверяем, есть ли у пользователя продукты
        List<Product> userProducts = productRepository.findByUserId(id);
        if (!userProducts.isEmpty()) {
            throw new UserDeletedException("Cannot delete user with id: " + id +
                    " because they have " + userProducts.size() + " products");
        }

        try {
            userRepository.delete(user);
            log.info("User deleted successfully with id: {}", id);
        } catch (DataAccessException e) {
            log.error("Error deleting user with id: {}", id, e);
            throw new UserDeletedException("Failed to delete user with id: " + id, e);
        }
    }

    private void validateUserId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
    }
}
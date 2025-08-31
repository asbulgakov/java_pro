package ru.bulgakov.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.bulgakov.spring.exception.CreatedUserException;
import ru.bulgakov.spring.exception.DeletedUserException;
import ru.bulgakov.spring.exception.NotFoundUserException;
import ru.bulgakov.spring.exception.UpdatedUserException;
import ru.bulgakov.spring.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final DataSource dataSource;

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (username) VALUES (?) RETURNING id";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getUsername());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong("id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new CreatedUserException("Error creating user", e);
        }
        
        throw new RuntimeException("Failed to create user");
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, username FROM users WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setLong(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new NotFoundUserException("Error finding user by id", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, username FROM users ORDER BY id";
        List<User> users = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new NotFoundUserException("Error finding all users", e);
        }
        
        return users;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET username = ? WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getUsername());
            statement.setLong(2, user.getId());
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new NotFoundUserException("User not found with id: " + user.getId());
            }
            
            return user;
        } catch (SQLException e) {
            throw new UpdatedUserException("Error updating user", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setLong(1, id);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DeletedUserException("Error deleting user", e);
        }
    }
}
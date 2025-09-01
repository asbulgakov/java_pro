package ru.bulgakov.spring.converter;

import org.springframework.stereotype.Component;
import ru.bulgakov.spring.model.User;

@Component
public class UserConverter {
    public String userToString(User user) {
        return "Id: %d, FullName: %s".formatted(user.getId(), user.getUsername());
    }
}

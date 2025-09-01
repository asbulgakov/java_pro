package ru.bulgakov.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.bulgakov.spring.model.User;
import ru.bulgakov.spring.service.UserService;

import java.util.List;
import java.util.Optional;

@ComponentScan
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        UserService userService = context.getBean(UserService.class);

        System.out.println("=== Демонстрация CRUD операций с пользователями ===\n");


        // 1. Создаем пользователей
        System.out.println("1. Создаем пользователей:");
        User user1 = userService.createUser("john_doe");
        System.out.println("Создан пользователь: " + user1);

        User user2 = userService.createUser("jane_smith");
        System.out.println("Создан пользователь: " + user2);

        User user3 = userService.createUser("bob_wilson");
        System.out.println("Создан пользователь: " + user3);
        System.out.println();

        // 3. Получаем всех пользователей после создания
        System.out.println("3. Получаем всех пользователей после создания:");
        List<User> users = userService.getAllUsers();
        System.out.println("Количество пользователей: " + users.size());
        users.forEach(System.out::println);
        System.out.println();

        // 4. Получаем одного пользователя по ID
        System.out.println("4. Получаем пользователя по ID:");
        Long userId = user1.getId();
        Optional<User> foundUser = userService.getUserById(userId);
        if (foundUser.isPresent()) {
            System.out.println("Найден пользователь: " + foundUser.get());
        } else {
            System.out.println("Пользователь с ID " + userId + " не найден");
        }
        System.out.println();

        // 5. Обновляем пользователя
        System.out.println("5. Обновляем пользователя:");
        User updatedUser = userService.updateUser(user1.getId(), "jane_doe_updated");
        System.out.println("Обновлен пользователь: " + updatedUser);
        System.out.println();

        // 6. Получаем всех пользователей после обновления
        System.out.println("6. Получаем всех пользователей после обновления:");
        users = userService.getAllUsers();
        users.forEach(System.out::println);
        System.out.println();

        // 7. Удаляем пользователя
        System.out.println("7. Удаляем пользователя: " + user2.getUsername());
        boolean deleted = userService.deleteUser(user2.getId());
        System.out.println("Пользователь удален: " + deleted);
        System.out.println();

        // 8. Получаем всех пользователей после удаления
        System.out.println("8. Получаем всех пользователей после удаления:");
        users = userService.getAllUsers();
        System.out.println("Количество пользователей: " + users.size());
        users.forEach(System.out::println);
        System.out.println();

        // 9. Попытка получить удаленного пользователя
        System.out.println("9. Попытка получить удаленного пользователя:");
        Optional<User> deletedUser = userService.getUserById(user2.getId());
        if (deletedUser.isPresent()) {
            System.out.println("Найден пользователь: " + deletedUser.get());
        } else {
            System.out.println("Пользователь с ID " + user2.getId() + " не найден (удален)");
        }

        System.out.println("\n=== Все операции выполнены успешно! ===");
    }
}

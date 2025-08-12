package ru.bulgakov;

import ru.bulgakov.runner.TestRunner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Запуск тестов ExampleTestClass ===");
        TestRunner.runTests(ExampleTestClass.class);
    }
}
package ru.bulgakov;

import lombok.extern.slf4j.Slf4j;
import ru.bulgakov.annotation.AfterSuite;
import ru.bulgakov.annotation.AfterTest;
import ru.bulgakov.annotation.BeforeSuite;
import ru.bulgakov.annotation.BeforeTest;
import ru.bulgakov.annotation.CsvSource;
import ru.bulgakov.annotation.Test;
@Slf4j
public class ExampleTestClass {
    private static int suiteCounter = 0;
    private int testCounter = 0;

    @BeforeSuite
    public static void beforeSuite() {
        suiteCounter++;
        log.info("Инициализация тестового набора. Счетчик: {}", suiteCounter);
    }

    @AfterSuite
    public static void afterSuite() {
        log.info("Завершение тестового набора. Всего выполнено наборов: {}", suiteCounter);
    }

    @BeforeTest
    public void beforeTest() {
        testCounter++;
        log.info("Подготовка к тесту #{}", testCounter);
    }

    @AfterTest
    public void afterTest() {
        log.info("Очистка после теста #{}", testCounter);
    }

    @Test(priority = 10)
    public void highPriorityTest() {
        log.info("Выполняется тест с высоким приоритетом");
        assert 2 + 2 == 4 : "Математика не работает!";
    }

    @Test(priority = 1)
    public void lowPriorityTest() {
        log.info("Выполняется тест с низким приоритетом");
        String str = "Hello World";
        assert str.length() == 11 : "Неверная длина строки";
    }

    @Test(priority = 5)
    public void mediumPriorityTest() {
        log.info("Выполняется тест со средним приоритетом");
        boolean condition = true;
        assert condition : "Условие должно быть истинным";
    }

    @Test(priority = 8)
    public void failingTest() {
        log.info("Выполняется тест, который должен упасть");
        throw new RuntimeException("Этот тест специально падает для демонстрации");
    }

    @Test(priority = 7)
    @CsvSource("10,20,30")
    public void testWithCsvSource(int a, int b, int expected) {
        log.info("Тестируем сложение: {} + {} = {}", a, b, expected);
        int result = a + b;
        assert result == expected : String.format("Ожидали %d, получили %d", expected, result);
    }

    @Test(priority = 6)
    @CsvSource("\"Hello\",5")
    public void testStringLength(String text, int expectedLength) {
        log.info("Проверяем длину строки '{}', ожидаем {}", text, expectedLength);
        assert text.length() == expectedLength :
                String.format("Длина строки '%s' должна быть %d, но получили %d",
                        text, expectedLength, text.length());
    }

    @Test(priority = 4)
    @CsvSource("true,false")
    public void testBooleanValues(boolean value1, boolean value2) {
        log.info("Тестируем булевы значения: {} и {}", value1, value2);
        assert value1 != value2 : "Значения должны быть разными";
    }

    @Test(priority = 3)
    @CsvSource("3.14,2.71")
    public void testDoubleValues(double pi, double e) {
        log.info("Тестируем числа с плавающей точкой: π={}, e={}", pi, e);
        assert pi > e : "π должно быть больше e";
        assert Math.abs(pi - 3.14) < 0.01 : "π должно быть приблизительно равно 3.14";
    }

    @Test(priority = 2)
    public void anotherMediumPriorityTest() {
        log.info("Еще один тест со средним приоритетом (для проверки сортировки по имени)");
        int[] array = {1, 2, 3, 4, 5};
        assert array.length == 5 : "Массив должен содержать 5 элементов";
    }
}

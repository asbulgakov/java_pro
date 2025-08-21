package ru.bulgakov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.bulgakov.Employee.Position.DEVELOPER;
import static ru.bulgakov.Employee.Position.DIRECTOR;
import static ru.bulgakov.Employee.Position.ENGINEER;
import static ru.bulgakov.Employee.Position.MANAGER;

public class Main {

    static List<Integer> integers = List.of(5, 2, 10, 9, 4, 3, 10, 1, 13);

    static List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("Василий", 22, MANAGER),
            new Employee("Анатолий", 31, ENGINEER),
            new Employee("Николай", 48, MANAGER),
            new Employee("Александр", 25, DEVELOPER),
            new Employee("Инна", 33, DIRECTOR),
            new Employee("Татьяна", 27, ENGINEER),
            new Employee("Сергей", 19, MANAGER),
            new Employee("Вячеслав", 36, ENGINEER),
            new Employee("Анастасия", 29, MANAGER),
            new Employee("Олег", 30, DEVELOPER)
    ));

    static List<String> words = List.of(
            "class", "consumer", "pattern", "build", "push"
    );

    static String inputString = "java stream api java collection stream java api";

    static String[] arr = {
            "tree grass flower seaweed bush",
            "sand earth stone asphalt dirt"
    };

    public static void main(String[] args) {
        StreamTask task = new StreamTask();

        task.getThirdLargestNumber(integers);

        task.getThirdLargestUniqueNumber(integers);

        task.getNameListThreeOldestEmployeesWithPosition(employees, ENGINEER);

        task.getAverageAgeEmployeesWithPosition(employees, ENGINEER);

        task.getLongestWord(words);

        task.getMapOfWordCount(inputString);

        task.printStringsFromListAsc(words);

        task.getLongestWordFromArray(arr);
    }
}

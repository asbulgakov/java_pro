package ru.bulgakov;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTask {
    /**
     * Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10)
     */
    public Integer getThirdLargestNumber(List<Integer> integers) {
        return integers.stream()
                .sorted((a, b) -> Integer.compare(b, a))
                .limit(3)
                .skip(2)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Найдите в списке целых чисел 3-е наибольшее «уникальное» число
     * (пример: 5 2 10 9 4 3 10 1 13 => 9, в отличие от прошлой задачи здесь разные 10 считает за одно число)
     */
    public Integer getThirdLargestUniqueNumber(List<Integer> integers) {
        return integers.stream()
                .distinct()
                .sorted((a, b) -> Integer.compare(b, a))
                .limit(3)
                .skip(2)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить список имен
     * 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста
     */
    public List<String> getNameListThreeOldestEmployeesWithPosition(
            List<Employee> employees,
            Employee.Position position
    ) {
        return employees.stream()
                .filter(employee -> employee.getPosition() == position)
                .sorted(Collections.reverseOrder(Comparator.comparing(Employee::getAge)))
                .limit(3)
                .map(Employee::getName)
                .toList();
    }

    /**
     * Имеется список объектов типа Сотрудник (имя, возраст, должность),
     * посчитайте средний возраст сотрудников с должностью «Инженер»
     */
    public double getAverageAgeEmployeesWithPosition(List<Employee> employees, Employee.Position position) {
        return employees.stream()
                .filter(employee -> employee.getPosition() == position)
                .mapToInt(Employee::getAge)
                .average()
                .orElse(0.0);
    }

    /**
     * Найдите в списке слов самое длинное
     */
    public String getLongestWord(List<String> words) {
        return words.stream()
                .max(Comparator.comparing(String::length))
                .orElse("");
    }

    /**
     * Имеется строка с набором слов в нижнем регистре, разделенных пробелом.
     * Постройте хеш-мапы, в которой будут хранится пары: слово - сколько раз оно встречается
     * во входной строке
     */
    public Map<String, Long> getMapOfWordCount(String inputString) {
        return Arrays.stream(inputString.split(" "))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }

    /**
     * Отпечатайте в консоль строки из списка в порядке увеличения длины слова,
     * если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок
     */
    public void printStringsFromListAsc(List<String> words) {
        words.stream()
                .sorted(Comparator.comparing(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .forEach(System.out::println);
    }

    /**
     * Имеется массив строк, в каждой из которых лежит набор из 5 слов, разделенных пробелом,
     * найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
     */
    public String getLongestWordFromArray(String[] arr) {
        return Stream.of(arr)
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .max(Comparator.comparing(String::length))
                .orElse("Нет слов");
    }
}

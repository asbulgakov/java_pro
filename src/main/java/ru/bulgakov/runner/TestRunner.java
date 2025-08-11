package ru.bulgakov.runner;

import lombok.extern.slf4j.Slf4j;
import ru.bulgakov.annotation.AfterSuite;
import ru.bulgakov.annotation.AfterTest;
import ru.bulgakov.annotation.BeforeSuite;
import ru.bulgakov.annotation.BeforeTest;
import ru.bulgakov.annotation.CsvSource;
import ru.bulgakov.annotation.Test;
import ru.bulgakov.converter.TypeConverterRegistry;
import ru.bulgakov.exception.ConversionException;
import ru.bulgakov.exception.TestCreationException;
import ru.bulgakov.exception.TestMethodException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class TestRunner {
    public static void runTests(Class<?> c) {
        TestContext context = createTestContext(c);
        TestMethods testMethods = collectTestMethods(context.testClass());

        executeAnnotatedMethod(testMethods.beforeSuiteMethod(), context.testInstance(), "@BeforeSuite");

        TestResults results = executeTestMethods(
                testMethods.testMethods(),
                testMethods.beforeTestMethods(),
                testMethods.afterTestMethods(),
                context.testInstance()
        );

        executeAnnotatedMethod(testMethods.afterSuiteMethod(), context.testInstance(), "@AfterSuite");

        logTestResults(results);
    }

    private static TestContext createTestContext(Class<?> c) {
        try {
            Class<?> testClass = Class.forName(c.getName());
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
            return new TestContext(testClass, testInstance);
        } catch (Exception e) {
            throw new TestCreationException("Test must have empty constructor", e);
        }
    }

    private static TestMethods collectTestMethods(Class<?> testClass) {
        Method[] methods = testClass.getDeclaredMethods();

        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        List<Method> beforeTestMethods = new ArrayList<>();
        List<Method> afterTestMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                checkDuplicate(beforeSuiteMethod, "@BeforeSuite");
                validateStaticMethod(method, "@BeforeSuite");
                beforeSuiteMethod = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                checkDuplicate(afterSuiteMethod, "@AfterSuite");
                validateStaticMethod(method, "@AfterSuite");
                afterSuiteMethod = method;
            } else if (method.isAnnotationPresent(BeforeTest.class)) {
                beforeTestMethods.add(method);
            } else if (method.isAnnotationPresent(AfterTest.class)) {
                afterTestMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                validateTestPriority(method);
                testMethods.add(method);
            }
        }

        return new TestMethods(beforeSuiteMethod, afterSuiteMethod, beforeTestMethods, afterTestMethods, testMethods);
    }

    private static TestResults executeTestMethods(
            List<Method> testMethods,
            List<Method> beforeTestMethods,
            List<Method> afterTestMethods,
            Object testInstance
    ) {
        int passedTests = 0;
        int failedTests = 0;

        for (Method testMethod : getSortedTestMethods(testMethods)) {
            for (Method beforeTestMethod : beforeTestMethods) {
                executeAnnotatedMethod(beforeTestMethod, testInstance, "@BeforeTest");
            }

            logTestExecution(testMethod);

            if (executeTestMethod(testMethod, testInstance)) {
                passedTests++;
                log.info("Тест {} прошел успешно.", testMethod.getName());
            } else {
                failedTests++;
            }

            for (Method afterTestMethod : afterTestMethods) {
                executeAnnotatedMethod(afterTestMethod, testInstance, "@AfterTest");
            }
        }

        return new TestResults(passedTests, failedTests);
    }

    private static void logTestExecution(Method testMethod) {
        log.info("""
                Выполняется: \s
                @Test: {},  \s
                Приоритет: {}""",
                testMethod.getName(),
                testMethod.getAnnotation(Test.class).priority()
        );
    }

    private static boolean executeTestMethod(Method testMethod, Object testInstance) {
        try {
            if (testMethod.isAnnotationPresent(CsvSource.class)) {
                return executeTestMethodWithCsvSource(testMethod, testInstance);
            } else {
                testMethod.invoke(testInstance);
                return true;
            }
        } catch (IllegalAccessException e) {
            throw new TestMethodException("Test method must be public", e);
        } catch (InvocationTargetException e) {
            handleInvocationTargetException(testMethod, e);
            return false;
        } catch (Exception e) {
            errorExecutingLog(testMethod, e);
            return false;
        }
    }

    private static boolean executeTestMethodWithCsvSource(Method testMethod, Object testInstance) throws Exception {
        CsvSource csvSource = testMethod.getAnnotation(CsvSource.class);
        String csvData = csvSource.value();

        Parameter[] parameters = testMethod.getParameters();
        String[] csvValues = parseCsvLine(csvData);

        if (csvValues.length != parameters.length) {
            throw new TestMethodException("CSV values count (" + csvValues.length +
                    ") doesn't match method parameters count (" + parameters.length + ")", (IllegalAccessException) null);
        }

        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            try {
                args[i] = TypeConverterRegistry.convert(csvValues[i].trim(), parameters[i].getType());
            } catch (ConversionException e) {
                throw new TestMethodException("Failed to convert CSV value '" + csvValues[i] +
                        "' to type " + parameters[i].getType().getSimpleName() + ": " + e.getMessage(), e);
            }
        }

        testMethod.invoke(testInstance, args);
        return true;
    }

    private static String[] parseCsvLine(String csvLine) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString());

        return values.toArray(new String[0]);
    }

    private static void handleInvocationTargetException(Method testMethod, InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            errorExecutingLog(testMethod, e);
        } else {
            throw new RuntimeException(e);
        }
    }

    private static void errorExecutingLog(Method testMethod, Exception e) {
        log.error("Ошибка при выполнении теста {}: {}", testMethod.getName(), e.getMessage());
    }

    private static void logTestResults(TestResults results) {
        int totalTests = results.passedTests() + results.failedTests();
        log.info("Всего тестов: {}", totalTests);
        log.info("Успешных тестов: {}", results.passedTests());
        log.info("Неуспешных тестов: {}", results.failedTests());
    }

    private static void executeAnnotatedMethod(Method annotatedMethod, Object testInstance, String annotation) {
        if (annotatedMethod != null) {
            log.info("Выполняется {}: {}", annotation, annotatedMethod.getName());
            try {
                if (java.lang.reflect.Modifier.isStatic(annotatedMethod.getModifiers())) {
                    annotatedMethod.invoke(null);
                } else {
                    annotatedMethod.invoke(testInstance);
                }
            } catch (IllegalAccessException e) {
                throw new TestMethodException("Test method must be public", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void checkDuplicate(Method existingMethod, String annotation) {
        if (existingMethod != null) {
            throw new RuntimeException("Найдено более одного метода с аннотацией " + annotation);
        }
    }

    private static void validateStaticMethod(Method method, String annotation) {
        if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
            throw new RuntimeException("Метод с аннотацией " + annotation + " должен быть статическим");
        }
    }

    private static void validateTestPriority(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        int priority = testAnnotation.priority();
        if (priority < 1 || priority > 10) {
            throw new RuntimeException("Приоритет теста должен быть в диапазоне от 1 до 10, получен: " + priority);
        }
    }

    private static List<Method> getSortedTestMethods(List<Method> testMethods) {
        return testMethods.stream()
                .filter(m -> m.isAnnotationPresent(Test.class))
                .sorted(Comparator.comparingInt(
                                (Method method) -> method.getAnnotation(Test.class).priority())
                        .reversed()
                        .thenComparing(Method::getName))
                .toList();
    }

    private record TestContext(Class<?> testClass, Object testInstance) {}

    private record TestMethods(
        Method beforeSuiteMethod,
        Method afterSuiteMethod,
        List<Method> beforeTestMethods,
        List<Method> afterTestMethods,
        List<Method> testMethods
    ) {}

    private record TestResults(int passedTests, int failedTests) {}
}
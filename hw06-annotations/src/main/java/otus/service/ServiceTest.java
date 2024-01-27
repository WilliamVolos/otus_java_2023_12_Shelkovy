package otus.service;

import otus.annotations.After;
import otus.annotations.Before;
import otus.annotations.Test;

import java.lang.reflect.Method;

public class ServiceTest {
    private static int allTestsCount;
    private static int passedTestsCount;
    private static int failedTestsCount;
    private ServiceTest() {}

    public static void runTestClass(Class<?> targetClass){

        StringBuilder testsInfo = new StringBuilder();

        for (Method testMethod : Reflection.getMethodsByAnnotation(targetClass, Test.class)){
            Object instClass = null;
            allTestsCount++;

            try {
                instClass = Reflection.instantiate(targetClass);
            } catch (RuntimeException e) {
                System.out.printf("Ошибка запуска экземпляра класса: \n%s\n",e);
                return;
            }

            // Запускаем сначала все Before методы
            for (Method beforeMethod : Reflection.getMethodsByAnnotation(targetClass, Before.class)){
                runUtilityMethod(instClass, beforeMethod);
            }

            // Затем запускаем Тест метод
            if (runTestMethod(instClass, testMethod, testsInfo)) {
                passedTestsCount++;
            } else {
                failedTestsCount++;
            }

            // После этого запускаем все After методы
            for (Method afterMethod : Reflection.getMethodsByAnnotation(targetClass, After.class)){
                runUtilityMethod(instClass, afterMethod);
            }
        }

        testsInfo.insert(0, String.format("Запуск тестового класса: %s \n" +
                "Тестов прошло успешно: %d\n" +
                "Неудачно выполненных: %d\n" +
                "Всего тестов: %d\n",
                targetClass.getSimpleName(),
                passedTestsCount,
                failedTestsCount,
                allTestsCount));

        System.out.println(testsInfo);
    }

    private static void runUtilityMethod(Object instClass, Method method){
        try {
            Reflection.callMethod(instClass, method.getName());
        } catch (RuntimeException e) {
            System.out.printf("Ошибка запуска метода: %s\n%s\n%n",
                              method.getName(), e);
        }
    }

    private static boolean runTestMethod(Object instClass, Method method, StringBuilder testsInfo){
        try {
            Reflection.callMethod(instClass, method.getName());
            testsInfo.append("Тест: ")
                    .append(method.getName())
                    .append(" - выполнен успешно!\n");
            return true;
        } catch (RuntimeException e) {
            testsInfo.append("Тест: ")
                    .append(method.getName())
                    .append(" - не пройден!")
                    .append("\n")
                    .append(e)
                    .append("\n");
            return false;
        }
    }
}

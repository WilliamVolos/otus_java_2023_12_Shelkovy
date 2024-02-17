package ru.otus.aop.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Reflection {
    private Reflection() {}

    public static Method[] getMethodsByAnnotation(Class<?> targetClass, Class<? extends Annotation> annotation) {

        return Arrays.stream(targetClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotation))
                .toArray(Method[]::new);
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}

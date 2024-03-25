package ru.otus.jdbc.mapper;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.otus.jdbc.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T>{
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            Class<?>[] fields = Arrays.stream(clazz.getDeclaredFields())
                    .map(Field::getType).toArray(sz-> new Class<?>[sz]);
            return clazz.getDeclaredConstructor(fields);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f->f.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow();
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f->!f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}

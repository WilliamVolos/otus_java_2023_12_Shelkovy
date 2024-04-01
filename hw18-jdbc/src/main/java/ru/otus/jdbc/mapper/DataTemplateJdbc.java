package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.core.repository.DataTemplate;
import ru.otus.jdbc.core.repository.DataTemplateException;
import ru.otus.jdbc.core.repository.executor.DbExecutor;

import static java.util.stream.Collectors.toCollection;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityMetaData = entityMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs ->{
            try {
                if (rs.next()) {
                    try{
                        List<Object> fields = new ArrayList<>();
                        for (Field field : entityMetaData.getAllFields()){
                            fields.add(rs.getObject(field.getName()));
                        }
                        return entityMetaData.getConstructor().newInstance(fields.toArray());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs ->{
            try {
                List<T> resultList = new ArrayList<>();
                while (rs.next()) {
                    try{
                        List<Object> fields = new ArrayList<>();
                        for (Field field : entityMetaData.getAllFields()){
                            fields.add(rs.getObject(field.getName()));
                        }
                        resultList.add(entityMetaData.getConstructor().newInstance(fields.toArray()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return resultList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElse(null);
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> params = Arrays.stream(client.getClass().getDeclaredFields())
                            .filter(f-> !f.isAnnotationPresent(Id.class))
                            .map(f-> {
                                try {
                                    f.setAccessible(true);
                                    return f.get(client);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }).toList();
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> params = Arrays.stream(client.getClass().getDeclaredFields())
                    .filter(f-> !f.isAnnotationPresent(Id.class))
                    .map(f-> {
                        try {
                            f.setAccessible(true);
                            return f.get(client);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(toCollection(ArrayList::new));
            Object paramID = Arrays.stream(client.getClass().getDeclaredFields())
                    .filter(f-> f.isAnnotationPresent(Id.class))
                    .map(f-> {
                        try {
                            f.setAccessible(true);
                            return f.get(client);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }).findFirst().orElse(null);
            if(paramID != null){
                params.addLast(paramID);
            }else throw new RuntimeException("Не задан первичный ключ в таблице");
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}

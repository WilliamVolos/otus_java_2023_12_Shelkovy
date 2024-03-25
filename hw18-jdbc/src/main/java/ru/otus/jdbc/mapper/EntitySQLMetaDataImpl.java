package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{
    private final EntityClassMetaData<?> entityMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityMetaData) {
        this.entityMetaData = entityMetaData;
    }

    @Override
    public String getSelectAllSql() {
        StringBuilder select = new StringBuilder();
        select.append("Select ");
        select.append(getFields(false));
        select.append(" from ");
        select.append(entityMetaData.getName());
        return select.toString();
    }

    @Override
    public String getSelectByIdSql() {
        StringBuilder select = new StringBuilder();
        select.append(getSelectAllSql());
        select.append(" where ");
        select.append(entityMetaData.getIdField().getName());
        select.append(" = ?");
        return select.toString();
    }

    @Override
    public String getInsertSql() {
        StringBuilder insert = new StringBuilder();
        insert.append("Insert into ");
        insert.append(entityMetaData.getName());
        insert.append("(");
        insert.append(getFields(true));
        insert.append(")");
        insert.append(" values (");
        for(int i=0; i<entityMetaData.getFieldsWithoutId().size();i++){
            if (i>0) {
                insert.append(", ");
            }
            insert.append("?");
        }
        insert.append(")");
        return insert.toString();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder update = new StringBuilder();
        update.append("Update ");
        update.append(entityMetaData.getName());
        update.append(" set");
        for(Field field : entityMetaData.getFieldsWithoutId()){
            update.append(" ");
            update.append(field.getName());
            update.append(" = ?,");
        }
        update.deleteCharAt(update.length()-1);
        update.append(" where ");
        update.append(entityMetaData.getIdField().getName());
        update.append(" = ?");
        return update.toString();
    }

    private String getFields(boolean withoutID){
        Stream<Field> stream;
        if(withoutID){
            stream = entityMetaData.getFieldsWithoutId().stream();
        }else{
            stream = entityMetaData.getAllFields().stream();
        };

        String fields = stream
                        .map(s-> s.getName()+", ")
                        .collect(Collectors.joining());

        if (fields.length()>0) {
            fields = fields.substring(0, fields.length() - 2);
        }else throw new NoSuchElementException("Нет полей в классе");

        return fields;
    }
}

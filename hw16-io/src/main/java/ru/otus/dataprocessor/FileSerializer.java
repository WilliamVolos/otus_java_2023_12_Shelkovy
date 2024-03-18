package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(FileSerializer.class);
    private final String fileName;
    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.mapper = JsonMapper.builder().build();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try{
            var file = new File(fileName);
            mapper.writeValue(file, data);
        }catch (IOException ex){
            logger.error(ex.getMessage());
        }
    }
}

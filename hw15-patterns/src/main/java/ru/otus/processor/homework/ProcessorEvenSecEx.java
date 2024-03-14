package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorEvenSecEx implements Processor {
    private final DateTimeProvider timeProvider;
    public ProcessorEvenSecEx(DateTimeProvider timeProvider){
        this.timeProvider = timeProvider;
    }
    @Override
    public Message process(Message message) {
        if ((timeProvider.getTime().getSecond() % 2) == 0){
            throw new IllegalStateException("Процессор создан в четную секунду: "+timeProvider.getTime().getSecond());
        }
        return message;
    }
}

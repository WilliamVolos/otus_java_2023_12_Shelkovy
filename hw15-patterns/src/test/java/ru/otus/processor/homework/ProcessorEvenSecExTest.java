package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@DisplayName("Процессор ProcessorEvenSecExTest должен:")
public class ProcessorEvenSecExTest {

    @Test
    @DisplayName("Выбрасывать исключение в четную секунду")
    void processorEvenSecondExceptionTest(){
        var message = mock(Message.class);
        Processor processor = new ProcessorEvenSecEx(()->LocalDateTime.of(2024,3,14,10,30,50));
        assertThrows(IllegalStateException.class, ()->processor.process(message));
    }

    @Test
    @DisplayName("Не выбрасывать исключение в нечетную секунду")
    void processorOddSecondTest(){
        var message = mock(Message.class);
        Processor processor = new ProcessorEvenSecEx(()->LocalDateTime.of(2024,3,14,10,30,51));
        assertDoesNotThrow(()->processor.process(message));
    }
}

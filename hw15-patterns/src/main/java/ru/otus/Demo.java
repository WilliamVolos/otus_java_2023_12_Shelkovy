package ru.otus;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.ProcessorUpperField10;
import ru.otus.processor.homework.ProcessorChangeFields;
import ru.otus.processor.homework.ProcessorEvenSecEx;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(), new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(listenerPrinter);
    }
}

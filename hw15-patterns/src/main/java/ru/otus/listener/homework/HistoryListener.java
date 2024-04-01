package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, Message> historyMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message message = msg.toBuilder().build();
        historyMap.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(historyMap.get(id));
    }
}

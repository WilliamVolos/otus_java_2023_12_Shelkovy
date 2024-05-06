package ru.otus.webserver.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import ru.otus.webserver.model.User;

public class InMemoryUserDao implements UserDao {

    public static final String DEFAULT_PASSWORD = "admin";
    private final Random random = new Random();
    private final Map<Long, User> users;

    public InMemoryUserDao() {
        users = new HashMap<>();
        users.put(1L, new User(1L, "Администратор", "admin", DEFAULT_PASSWORD));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream().filter(v -> v.getLogin().equals(login)).findFirst();
    }
}

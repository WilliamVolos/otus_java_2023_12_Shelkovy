package ru.otus.webserver.dao;

import java.util.Optional;
import ru.otus.webserver.model.User;

public interface UserDao {
    Optional<User> findByLogin(String login);
}

package ru.otus.jdbc.core.sessionmanager;

import java.sql.Connection;
import java.util.function.Function;

public interface TransactionRunner {

    <T> T doInTransaction(TransactionAction<T> action);
}

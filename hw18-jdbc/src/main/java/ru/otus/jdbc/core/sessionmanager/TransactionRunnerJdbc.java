package ru.otus.jdbc.core.sessionmanager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class TransactionRunnerJdbc implements TransactionRunner {
    private final DataSource dataSource;

    public TransactionRunnerJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T doInTransaction(TransactionAction<T> action) {
        return wrapException(() -> {
            try (var connection = dataSource.getConnection()) {
                try {
                    var result = action.apply(connection);
                    connection.commit();
                    return result;
                } catch (SQLException ex) {
                    connection.rollback();
                    throw new DataBaseOperationException("doInTransaction exception", ex);
                }
            }
        });
    }

    private <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception ex) {
            throw new DataBaseOperationException("exception", ex);
        }
    }
}

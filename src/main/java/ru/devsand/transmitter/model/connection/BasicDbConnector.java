package ru.devsand.transmitter.model.connection;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class BasicDbConnector implements DbConnector<ConnectionSource> {

    private static final String URL = "jdbc:sqlite::memory:";

    private ConnectionSource connectionSource;

    public BasicDbConnector() throws SQLException {
        connectionSource = new JdbcPooledConnectionSource(URL);
    }

    @Override
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    @Override
    public void close() throws Exception {
        connectionSource.close();
    }
}

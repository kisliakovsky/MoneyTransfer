package ru.devsand.transmitter.model.connection;

public interface DbConnector<C> extends AutoCloseable {

    C getConnectionSource();

}

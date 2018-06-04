package ru.devsand.transmitter.model.repository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public interface Repository<E, I> {

    void add(E subEntity) throws SQLException;

    void addAll(Collection<E> entities) throws SQLException;

    List<E> findAll() throws SQLException;

    E findOne(I id) throws SQLException;

    void update(E entity) throws SQLException;

    void delete(I id) throws SQLException;

    boolean exists(I id) throws SQLException;

    long count() throws SQLException;

    void forEach(Consumer<E> action);

    <T> T performTransaction(Callable<T> action) throws SQLException;

}

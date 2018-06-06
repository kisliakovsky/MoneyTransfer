package ru.devsand.transmitter.model.repository;

import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public abstract class AbstractRepository<E, I> implements Repository<E, I> {

    protected Dao<E, I> innerRepository;
    private TransactionManager transactionManager;

    public AbstractRepository(ConnectionSource connectionSource, Class<E> c)
            throws SQLException {
        this.transactionManager = new TransactionManager(connectionSource);
        this.innerRepository = DaoManager.createDao(connectionSource, c);
    }

    @Override
    public void add(E entity) throws SQLException {
        innerRepository.create(entity);
    }

    @Override
    public void addAll(Collection<E> entities) throws SQLException {
        for (E entity: entities) {
            innerRepository.create(entity);
        }
    }

    @Override
    public List<E> findAll() throws SQLException {
        return innerRepository.queryForAll();
    }

    @Override
    public E findOne(I id) throws SQLException {
        return innerRepository.queryForId(id);
    }

    @Override
    public void update(E entity) throws SQLException {
        innerRepository.update(entity);
    }

    @Override
    public void delete(I id) throws SQLException {
        innerRepository.deleteById(id);
    }

    @Override
    public boolean exists(I id) throws SQLException {
        return innerRepository.idExists(id);
    }

    @Override
    public long count() throws SQLException {
        return innerRepository.countOf();
    }

    @Override
    public void forEach(Consumer<E> action) {
        try(CloseableWrappedIterable<E> wrappedIterable = innerRepository.getWrappedIterable()) {
            wrappedIterable.forEach(action);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T performTransaction(Callable<T> action) throws SQLException {
        return transactionManager.callInTransaction(action);
    }

}

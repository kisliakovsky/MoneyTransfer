package ru.devsand.transmitter.model.repository;

import java.util.List;

public interface Repository<E, I> {

    <S extends E> S add(S subEntity);

    List<E> findAll();

    E findOne(I id);

    <S extends E> void update(S subEntity);

    void delete(I id);

    boolean exists(I id);

    long count();

}

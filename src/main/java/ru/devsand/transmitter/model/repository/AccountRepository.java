package ru.devsand.transmitter.model.repository;

import ru.devsand.transmitter.model.entity.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountRepository extends Repository<Account, Long> {

    List<Account> findByCustomerId(long customerId) throws SQLException;

}

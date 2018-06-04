package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Account;
import ru.devsand.transmitter.model.entity.Customer;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<Account> getAccountById(long id) throws SQLException;

    void addAccounts(Collection<Account> defaultAccounts) throws SQLException;

    void updateAccount(Account account) throws SQLException;

    List<Account> getAccountsByCustomerId(long customerId) throws SQLException;

    Optional<Account> getMostPriorityAccountByCustomerId(long customerId) throws SQLException;

}

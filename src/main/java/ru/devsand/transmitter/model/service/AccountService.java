package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Account;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<Account> getAccountById(long id) throws UnavailableDataException;

    void addAccounts(Collection<Account> defaultAccounts) throws UnableSaveException;

    void updateAccount(Account account) throws UnableSaveException;

    List<Account> getAccountsByCustomerId(long customerId) throws UnavailableDataException;

    Optional<Account> getMostPriorityAccountByCustomerId(long customerId) throws UnavailableDataException;

}

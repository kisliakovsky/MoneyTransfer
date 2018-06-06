package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Account;
import ru.devsand.transmitter.model.repository.AccountRepository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BasicAccountService implements AccountService {

    private AccountRepository accountRepository;

    public BasicAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> getAccountById(long id) throws UnavailableDataException {
        try {
            return Optional.ofNullable(accountRepository.findOne(id));
        } catch (SQLException e) {
            throw new UnavailableDataException();
        }
    }

    @Override
    public void addAccounts(Collection<Account> accounts) throws UnableSaveException {
        try {
            accountRepository.addAll(accounts);
        } catch (SQLException e) {
            throw new UnableSaveException();
        }
    }

    @Override
    public void updateAccount(Account account) throws UnableSaveException {
        try {
            accountRepository.update(account);
        } catch (SQLException e) {
            throw new UnableSaveException();
        }
    }

    @Override
    public List<Account> getAccountsByCustomerId(long customerId) throws UnavailableDataException {
        try {
            return accountRepository.findByCustomerId(customerId);
        } catch (SQLException e) {
            throw new UnavailableDataException();
        }
    }

    @Override
    public Optional<Account> getMostPriorityAccountByCustomerId(long customerId)
            throws UnavailableDataException {
        List<Account> customerAccounts = null;
        try {
            customerAccounts = accountRepository.findByCustomerId(customerId);
            return customerAccounts.stream().min(Comparator.comparingInt(Account::getPriority));
        } catch (SQLException e) {
            throw new UnavailableDataException();
        }

    }

}

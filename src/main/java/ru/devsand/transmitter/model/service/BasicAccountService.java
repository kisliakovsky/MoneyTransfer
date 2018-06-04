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
    public Optional<Account> getAccountById(long id) throws SQLException {
        return Optional.ofNullable(accountRepository.findOne(id));
    }

    @Override
    public void addAccounts(Collection<Account> accounts) throws SQLException {
        accountRepository.addAll(accounts);
    }

    @Override
    public void updateAccount(Account account) throws SQLException {
        accountRepository.update(account);
    }

    @Override
    public List<Account> getAccountsByCustomerId(long customerId) throws SQLException {
        return accountRepository.findByCustomerId(customerId);
    }

    @Override
    public Optional<Account> getMostPriorityAccountByCustomerId(long customerId) throws SQLException {
        List<Account> customerAccounts = accountRepository.findByCustomerId(customerId);
        return customerAccounts.stream().min(Comparator.comparingInt(Account::getPriority));
    }

}

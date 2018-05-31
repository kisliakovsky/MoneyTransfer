package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Account;
import ru.devsand.transmitter.model.entity.Customer;

import java.util.List;

public interface AccountService {

    Account findAccountById(long id);

    Account findMostPriorityAccountByCustomer(Customer customer);

    List<Account> findAllAccountsByCustomer(Customer customer);

}

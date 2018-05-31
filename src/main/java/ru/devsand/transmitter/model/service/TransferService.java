package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Customer;

public interface TransferService {

    void transferMoney(Customer sender, Customer receiver);

}

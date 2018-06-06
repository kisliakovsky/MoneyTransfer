package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Customer;

import java.util.Collection;
import java.util.Optional;

public interface CustomerService {

    void addCustomers(Collection<Customer> customers) throws UnableSaveException;

    Optional<Customer> getCustomerByPhoneNumber(String phoneNumber) throws UnableSaveException;

}

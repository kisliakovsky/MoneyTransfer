package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Customer;
import ru.devsand.transmitter.model.repository.CustomerRepository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class BasicCustomerService implements CustomerService {

    private CustomerRepository customerRepository;

    public BasicCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void addCustomers(Collection<Customer> customers) throws SQLException {
        customerRepository.addAll(customers);
    }

    @Override
    public Optional<Customer> getCustomerByPhoneNumber(String phoneNumber) throws SQLException {
        return Optional.ofNullable(customerRepository.findByPhoneNumber(phoneNumber));
    }

}

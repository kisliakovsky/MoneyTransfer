package ru.devsand.transmitter.model.repository;

import ru.devsand.transmitter.model.entity.Customer;

import java.sql.SQLException;

public interface CustomerRepository extends Repository<Customer, Long> {

    Customer findByPhoneNumber(String phoneNumber) throws SQLException;

}

package ru.devsand.transmitter.model.repository;

import com.j256.ormlite.support.ConnectionSource;
import ru.devsand.transmitter.model.entity.Customer;

import java.sql.SQLException;

public class BasicCustomerRepository extends AbstractRepository<Customer, Long>
        implements CustomerRepository {

    public BasicCustomerRepository(ConnectionSource connectionSource)
            throws SQLException {
        super(connectionSource, Customer.class);
    }

    @Override
    public Customer findByPhoneNumber(String phoneNumber) throws SQLException {
        return innerRepository.queryBuilder()
                .where().eq("phone_number", phoneNumber)
                .queryForFirst();
    }

}

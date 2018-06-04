package ru.devsand.transmitter.model.repository;

import com.j256.ormlite.support.ConnectionSource;
import ru.devsand.transmitter.model.entity.Account;
import ru.devsand.transmitter.model.entity.Currency;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class BasicAccountRepository extends AbstractRepository<Account, Long>
        implements AccountRepository {

    public BasicAccountRepository(ConnectionSource connectionSource)
            throws SQLException {
        super(connectionSource, Account.class);
    }

    @Override
    public void add(Account account) throws SQLException {
        checkAccount(account);
        super.add(account);
    }

    @Override
    public void addAll(Collection<Account> accounts) throws SQLException {
        for (Account acc: accounts) {
            checkAccount(acc);
        }
        for (Account acc: accounts) {
            innerRepository.create(acc);
        }
    }

    private void checkAccount(Account account) throws SQLException {
        checkPriority(account);
        checkCurrency(account);
    }

    private void checkPriority(Account account) throws SQLException {
        final int priority = account.getPriority();
        final long customerId = account.getCustomer().getId();
        final List<Account> accounts = innerRepository.queryBuilder()
                .where().eq("customer_id", customerId)
                .and().eq("priority", priority).query();
        if (!accounts.isEmpty()) {
                    throw new SQLException("Unable to add an account with an existing priority");
        }
    }

    private void checkCurrency(Account account) throws SQLException {
        String currency = account.getCurrency();
        if (!Currency.contains(currency)) {
            throw new SQLException("Unable to add an account with an incorrect currency value");
        }
    }

    @Override
    public List<Account> findByCustomerId(long customerId) throws SQLException {
        return innerRepository.queryBuilder()
                .where().eq("customer_id", customerId).query();
    }

}

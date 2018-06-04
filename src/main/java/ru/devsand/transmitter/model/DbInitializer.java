package ru.devsand.transmitter.model;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ru.devsand.transmitter.model.entity.Account;
import ru.devsand.transmitter.model.entity.Customer;
import ru.devsand.transmitter.model.entity.Transfer;
import ru.devsand.transmitter.model.repository.AccountRepository;
import ru.devsand.transmitter.model.repository.BasicAccountRepository;
import ru.devsand.transmitter.model.repository.BasicCustomerRepository;
import ru.devsand.transmitter.model.repository.CustomerRepository;
import ru.devsand.transmitter.model.service.AccountService;
import ru.devsand.transmitter.model.service.BasicAccountService;
import ru.devsand.transmitter.model.service.BasicCustomerService;
import ru.devsand.transmitter.model.service.CustomerService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DbInitializer {

    private static final List<Class<?>> TABLE_CLASSES = Arrays.asList(
            Customer.class, Account.class, Transfer.class
    );
    private static final List<Customer> DEFAULT_CUSTOMERS = Arrays.asList(
            new Customer("Ilia", "Kisliakovskii", "+79111111111"),
            new Customer("Ivan", "Berman", "+79122222222"),
            new Customer("Anton", "Lashichev", "+79055555555"),
            new Customer("Oleg", "Krivosheev", "+79101001010"),
            new Customer("Alexey", "Ivanov", "+79065566655")
    );
    private static final List<Account> DEFAULT_ACCOUNTS = Arrays.asList(
            new Account(DEFAULT_CUSTOMERS.get(0), BigDecimal.valueOf(1000), 1, "RUB"),
            new Account(DEFAULT_CUSTOMERS.get(0), BigDecimal.valueOf(2000), 2, "RUB"),
            new Account(DEFAULT_CUSTOMERS.get(1), BigDecimal.valueOf(2000), 1, "RUB"),
            new Account(DEFAULT_CUSTOMERS.get(2), BigDecimal.valueOf(3000), 1, "RUB"),
            new Account(DEFAULT_CUSTOMERS.get(3), BigDecimal.valueOf(3000), 1, "RUB"),
            new Account(DEFAULT_CUSTOMERS.get(4), BigDecimal.valueOf(3000), 1, "RUB")
    );

    public static void fillInDatabase(ConnectionSource connectionSource)
            throws SQLException {
        createTables(connectionSource);
        CustomerRepository customerRepository = new BasicCustomerRepository(connectionSource);
        CustomerService customerService = new BasicCustomerService(customerRepository);
        customerService.addCustomers(DEFAULT_CUSTOMERS);
        AccountRepository accountRepository = new BasicAccountRepository(connectionSource);
        AccountService accountService = new BasicAccountService(accountRepository);
        accountService.addAccounts(DEFAULT_ACCOUNTS);
    }

    private static void createTables(ConnectionSource connectionSource) throws SQLException {
        for (Class<?> c: TABLE_CLASSES) {
            TableUtils.createTable(connectionSource, c);
        }
    }

}

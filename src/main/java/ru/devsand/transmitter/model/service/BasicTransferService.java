package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Account;
import ru.devsand.transmitter.model.entity.Currency;
import ru.devsand.transmitter.model.entity.Customer;
import ru.devsand.transmitter.model.entity.Transfer;
import ru.devsand.transmitter.model.repository.AccountRepository;
import ru.devsand.transmitter.model.repository.CustomerRepository;
import ru.devsand.transmitter.model.repository.TransferRepository;
import ru.devsand.transmitter.thirdparty.ExchangeRateService;
import ru.devsand.transmitter.thirdparty.ExchangeRateServiceMock;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class BasicTransferService implements TransferService {

    private TransferRepository transferRepository;
    private AccountService accountService;
    private CustomerService customerService;
    private ExchangeRateService rateService;

    public BasicTransferService(TransferRepository transferRepository,
                                AccountRepository accountRepository,
                                CustomerRepository customerRepository) {
        this.transferRepository = transferRepository;
        this.accountService = new BasicAccountService(accountRepository);
        this.customerService = new BasicCustomerService(customerRepository);
        this.rateService = new ExchangeRateServiceMock();
    }

    @Override
    public Optional<Transfer> getTransfer(long id) throws SQLException {
        return Optional.ofNullable(transferRepository.findOne(id));
    }

    @Override
    public List<Transfer> getTransfers() throws SQLException {
        return transferRepository.findAll();
    }

    @Override
    public Transfer transferMoneyDirectly(long senderAccountId, long receiverAccountId,
                                          BigDecimal sum) throws SQLException {
        if (senderAccountId == receiverAccountId) {
            return pretendToTransferMoneyDirectly(senderAccountId, sum);
        }
        return transferRepository.performTransaction(() -> {
            Account senderAccount = accountService.getAccountById(senderAccountId)
                    .orElseThrow(AccountNotFoundException::new);
            Account receiverAccount = accountService.getAccountById(receiverAccountId)
                    .orElseThrow(AccountNotFoundException::new);
            return transferMoneyDirectlyHelper(senderAccount, receiverAccount, sum);
        });
    }

    private Transfer pretendToTransferMoneyDirectly(long accountId, BigDecimal sum)
            throws SQLException {
        return transferRepository.performTransaction(() -> {
            Account account = accountService.getAccountById(accountId)
                    .orElseThrow(AccountNotFoundException::new);
            return saveTransfer(account, account, sum);
        });
    }

    private Transfer transferZeroDirectly(long senderAccountId, long receiverAccountId)
            throws SQLException {
        return transferRepository.performTransaction(() -> {
            Account senderAccount = accountService.getAccountById(senderAccountId)
                    .orElseThrow(AccountNotFoundException::new);
            Account receiverAccount = accountService.getAccountById(receiverAccountId)
                    .orElseThrow(AccountNotFoundException::new);
            return saveTransfer(senderAccount, receiverAccount, BigDecimal.ZERO);
        });
    }

    private Transfer transferMoneyDirectlyHelper(Account senderAccount, Account receiverAccount,
                                                 BigDecimal sum) throws SQLException {
        updateSenderAccount(senderAccount, sum);
        Currency senderCurrency = Currency.valueOf(senderAccount.getCurrency());
        Currency receiverCurrency =  Currency.valueOf(receiverAccount.getCurrency());
        BigDecimal rate = rateService.obtainRate(senderCurrency, receiverCurrency);
        BigDecimal convertedSum = sum.multiply(rate);
        updateReceiverAccount(receiverAccount, convertedSum);
        return saveTransfer(senderAccount, receiverAccount, sum);
    }

    private void updateSenderAccount(Account senderAccount, BigDecimal sum)
            throws SQLException {
        BigDecimal senderBalance = senderAccount.getBalance();
        checkEnoughMoney(senderBalance, sum);
        senderAccount.setBalance(senderBalance.subtract(sum));
        accountService.updateAccount(senderAccount);
    }

    private static void checkEnoughMoney(BigDecimal senderBalance, BigDecimal sum)
            throws InsufficientFundsException {
        if (senderBalance.compareTo(sum) < 0) {
            throw new InsufficientFundsException();
        }
    }

    private void updateReceiverAccount(Account receiverAccount, BigDecimal sum)
            throws SQLException {
        BigDecimal receiverBalance = receiverAccount.getBalance();
        receiverAccount.setBalance(receiverBalance.add(sum));
        accountService.updateAccount(receiverAccount);
    }

    private Transfer saveTransfer(Account senderAccount, Account receiverAccount,
                                  BigDecimal sum) throws SQLException {
        Transfer transfer = new Transfer(senderAccount, receiverAccount, sum,
                new Timestamp(System.currentTimeMillis()));
        transferRepository.add(transfer);
        return transfer;
    }

    @Override
    public Transfer transferMoneyByPhoneNumber(String senderPhoneNumber, String receiverPhoneNumber,
                                               BigDecimal sum) throws SQLException {
        if (senderPhoneNumber.equals(receiverPhoneNumber)) {
            return pretendToTransferMoneyByPhoneNumber(senderPhoneNumber, sum);
        } else if (sum.equals(BigDecimal.ZERO)) {
            return transferZeroByPhoneNumber(senderPhoneNumber, receiverPhoneNumber);
        }
        return transferRepository.performTransaction(() -> {
            Account senderAccount = getAccountByPhoneNumber(senderPhoneNumber);
            Account receiverAccount = getAccountByPhoneNumber(receiverPhoneNumber);
            return transferMoneyDirectlyHelper(senderAccount, receiverAccount, sum);
        });
    }

    private Transfer pretendToTransferMoneyByPhoneNumber(String phoneNumber, BigDecimal sum)
            throws SQLException {
        return transferRepository.performTransaction(() -> {
            Account account = getAccountByPhoneNumber(phoneNumber);
            return saveTransfer(account, account, sum);
        });
    }

    private Transfer transferZeroByPhoneNumber(String senderPhoneNumber, String receiverPhoneNumber)
            throws SQLException {
        return transferRepository.performTransaction(() -> {
            Account senderAccount = getAccountByPhoneNumber(senderPhoneNumber);
            Account receiverAccount = getAccountByPhoneNumber(receiverPhoneNumber);
            return saveTransfer(senderAccount, receiverAccount, BigDecimal.ZERO);
        });
    }

    private Account getAccountByPhoneNumber(String phoneNumber) throws SQLException {
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber)
                .orElseThrow(CustomerNotFoundException::new);
        return accountService.getMostPriorityAccountByCustomerId(customer.getId())
                .orElseThrow(AccountNotFoundException::new);
    }

}

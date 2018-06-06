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
import java.util.concurrent.Callable;

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
    public Optional<Transfer> getTransfer(long id) throws UnavailableDataException {
        try {
            return Optional.ofNullable(transferRepository.findOne(id));
        } catch (SQLException e) {
            throw new UnavailableDataException();
        }
    }

    @Override
    public List<Transfer> getTransfers() throws UnavailableDataException {
        try {
            return transferRepository.findAll();
        } catch (SQLException e) {
            throw new UnavailableDataException();
        }
    }

    @Override
    public Transfer transferMoneyDirectly(long senderAccountId, long receiverAccountId,
                                          BigDecimal sum)
            throws UnableSaveException, InsufficientFundsException, UnavailableDataException {
        if (senderAccountId == receiverAccountId) {
            return pretendToTransferMoneyDirectly(senderAccountId, sum);
        }
        return performTransaction(() -> {
            Account senderAccount = accountService.getAccountById(senderAccountId)
                    .orElseThrow(AccountNotFoundException::new);
            Account receiverAccount = accountService.getAccountById(receiverAccountId)
                    .orElseThrow(AccountNotFoundException::new);
            return transferMoneyDirectlyHelper(senderAccount, receiverAccount, sum);
        });
    }

    private Transfer pretendToTransferMoneyDirectly(long accountId, BigDecimal sum)
            throws UnableSaveException, InsufficientFundsException, UnavailableDataException {
        return performTransaction(() -> {
            Account account = accountService.getAccountById(accountId)
                    .orElseThrow(AccountNotFoundException::new);
            return saveTransfer(account, account, sum);
        });
    }

    private Transfer transferMoneyDirectlyHelper(Account senderAccount, Account receiverAccount,
                                                 BigDecimal sum)
            throws InsufficientFundsException, UnableSaveException {
        updateSenderAccount(senderAccount, sum);
        Currency senderCurrency = Currency.valueOf(senderAccount.getCurrency());
        Currency receiverCurrency =  Currency.valueOf(receiverAccount.getCurrency());
        BigDecimal rate = rateService.obtainRate(senderCurrency, receiverCurrency);
        BigDecimal convertedSum = sum.multiply(rate);
        updateReceiverAccount(receiverAccount, convertedSum);
        return saveTransfer(senderAccount, receiverAccount, sum);
    }

    private void updateSenderAccount(Account senderAccount, BigDecimal sum)
            throws InsufficientFundsException, UnableSaveException {
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
            throws UnableSaveException {
        BigDecimal receiverBalance = receiverAccount.getBalance();
        receiverAccount.setBalance(receiverBalance.add(sum));
        accountService.updateAccount(receiverAccount);
    }

    private Transfer saveTransfer(Account senderAccount, Account receiverAccount,
                                  BigDecimal sum) throws UnableSaveException {
        try {
            Transfer transfer = new Transfer(senderAccount, receiverAccount, sum,
                    new Timestamp(System.currentTimeMillis()));
            transferRepository.add(transfer);
            return transfer;
        } catch (SQLException e) {
            throw new UnableSaveException();
        }
    }

    @Override
    public Transfer transferMoneyByPhoneNumber(String senderPhoneNumber, String receiverPhoneNumber,
                                               BigDecimal sum)
            throws UnableSaveException, InsufficientFundsException, UnavailableDataException {
        if (senderPhoneNumber.equals(receiverPhoneNumber)) {
            return pretendToTransferMoneyByPhoneNumber(senderPhoneNumber, sum);
        }
        return performTransaction(() -> {
            Account senderAccount = getAccountByPhoneNumber(senderPhoneNumber);
            Account receiverAccount = getAccountByPhoneNumber(receiverPhoneNumber);
            return transferMoneyDirectlyHelper(senderAccount, receiverAccount, sum);
        });
    }

    private Transfer pretendToTransferMoneyByPhoneNumber(String phoneNumber, BigDecimal sum)
            throws UnableSaveException, InsufficientFundsException, UnavailableDataException {
        return performTransaction(() -> {
            Account account = getAccountByPhoneNumber(phoneNumber);
            return saveTransfer(account, account, sum);
        });
    }

    private Transfer performTransaction(Callable<Transfer> transactionBody)
            throws UnableSaveException, UnavailableDataException, InsufficientFundsException {
        try {
            return transferRepository.performTransaction(transactionBody);
        } catch (SQLException e) {
            Throwable cause = e.getCause();
            if (cause instanceof CustomerNotFoundException) {
                throw (CustomerNotFoundException) cause;
            } else if (cause instanceof AccountNotFoundException) {
                throw (AccountNotFoundException) cause;
            } else if (cause instanceof InsufficientFundsException) {
                throw (InsufficientFundsException) cause;
            } else if (cause instanceof UnavailableDataException) {
                throw (UnavailableDataException) cause;
            }
            throw new UnableSaveException();
        }
    }

    private Account getAccountByPhoneNumber(String phoneNumber)
            throws UnavailableDataException, UnableSaveException {
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber)
                .orElseThrow(CustomerNotFoundException::new);
        return accountService.getMostPriorityAccountByCustomerId(customer.getId())
                .orElseThrow(AccountNotFoundException::new);
    }

}

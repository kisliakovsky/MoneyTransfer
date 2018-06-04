package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Account;
import ru.devsand.transmitter.model.entity.Customer;
import ru.devsand.transmitter.model.entity.Transfer;
import ru.devsand.transmitter.model.repository.AccountRepository;
import ru.devsand.transmitter.model.repository.CustomerRepository;
import ru.devsand.transmitter.model.repository.TransferRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class BasicTransferService implements TransferService {

    private TransferRepository transferRepository;
    private AccountService accountService;
    private CustomerService customerService;

    public BasicTransferService(TransferRepository transferRepository,
                                AccountRepository accountRepository,
                                CustomerRepository customerRepository) {
        this.transferRepository = transferRepository;
        this.accountService = new BasicAccountService(accountRepository);
        this.customerService = new BasicCustomerService(customerRepository);
    }

    @Override
    public Optional<Transfer> getTransfer(long id) throws SQLException {
        return Optional.ofNullable(transferRepository.findOne(id));
    }

    @Override
    public Transfer transferMoneyDirectly(long senderAccountId, long receiverAccountId,
                                          BigDecimal sum) throws SQLException {
        return transferRepository.performTransaction(() -> {
            Account senderAccount = accountService.getAccountById(senderAccountId)
                    .orElseThrow(AccountNotFoundException::new);
            Account receiverAccount = accountService.getAccountById(receiverAccountId)
                    .orElseThrow(AccountNotFoundException::new);
            return transferMoneyDirectlyHelper(senderAccount, receiverAccount, sum);
        });
    }

    private Transfer transferMoneyDirectlyHelper(Account senderAccount, Account receiverAccount,
                                                 BigDecimal sum) throws SQLException {
        updateSenderAccount(senderAccount, sum);
        updateReceiverAccount(receiverAccount, sum);
        return saveTransfer(senderAccount, receiverAccount, sum);
    }

    private Transfer saveTransfer(Account senderAccount, Account receiverAccount,
                                  BigDecimal sum) throws SQLException {
        Transfer transfer = new Transfer(senderAccount, receiverAccount, sum,
                new Timestamp(System.currentTimeMillis()));
        transferRepository.add(transfer);
        return transfer;
    }

    private void updateReceiverAccount(Account receiverAccount, BigDecimal sum)
            throws SQLException {
        BigDecimal receiverBalance = receiverAccount.getBalance();
        receiverAccount.setBalance(receiverBalance.add(sum));
        accountService.updateAccount(receiverAccount);
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

    @Override
    public Transfer transferMoneyByPhoneNumber(String senderPhoneNumber, String receiverPhoneNumber,
                                               BigDecimal sum) throws SQLException {
        return transferRepository.performTransaction(() -> {
            Account senderAccount = getAccountByPhoneNumber(senderPhoneNumber);
            Account receiverAccount = getAccountByPhoneNumber(receiverPhoneNumber);
            return transferMoneyDirectlyHelper(senderAccount, receiverAccount, sum);
        });
    }

    private Account getAccountByPhoneNumber(String phoneNumber) throws SQLException {
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber)
                .orElseThrow(CustomerNotFoundException::new);
        return accountService.getMostPriorityAccountByCustomerId(customer.getId())
                .orElseThrow(AccountNotFoundException::new);
    }

}

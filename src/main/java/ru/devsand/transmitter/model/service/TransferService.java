package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Transfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransferService {

    Optional<Transfer> getTransfer(long id) throws UnavailableDataException;

    List<Transfer> getTransfers() throws UnavailableDataException;

    Transfer transferMoneyDirectly(long senderAccountId, long receiverAccountId, BigDecimal sum) throws UnableSaveException, InsufficientFundsException, UnavailableDataException;

    Transfer transferMoneyByPhoneNumber(String senderPhoneNumber, String receiverPhoneNumber,
                                        BigDecimal sum) throws UnableSaveException, InsufficientFundsException, UnavailableDataException;

}

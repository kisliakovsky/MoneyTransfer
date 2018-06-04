package ru.devsand.transmitter.model.service;

import ru.devsand.transmitter.model.entity.Transfer;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public interface TransferService {

    Optional<Transfer> getTransfer(long id) throws SQLException;

    Transfer transferMoneyDirectly(long senderAccountId, long receiverAccountId, BigDecimal sum) throws SQLException;

    Transfer transferMoneyByPhoneNumber(String senderPhoneNumber, String receiverPhoneNumber,
                                        BigDecimal sum) throws SQLException;

}

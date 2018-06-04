package ru.devsand.transmitter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.support.ConnectionSource;
import ru.devsand.transmitter.model.entity.Transfer;
import ru.devsand.transmitter.model.repository.*;
import ru.devsand.transmitter.model.service.BasicTransferService;
import ru.devsand.transmitter.model.service.TransferService;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.eclipse.jetty.http.HttpStatus.Code.NOT_FOUND;


public class TransferController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final TransferService transferService;

    public TransferController(ConnectionSource connectionSource) throws SQLException {
        TransferRepository transferRepository = new BasicTransferRepository(connectionSource);
        AccountRepository accountRepository = new BasicAccountRepository(connectionSource);
        CustomerRepository customerRepository = new BasicCustomerRepository(connectionSource);
        transferService = new BasicTransferService(transferRepository, accountRepository,
                customerRepository);
    }

    public Object getTransfer(Request request, Response response)  {
        int id = Integer.valueOf(request.params(":id"));
        Transfer transfer = transferService.getTransfer(id);
        if (transfer != null) {
            return OBJECT_MAPPER.writeValueAsString(transfer);
        } else {
            response.status(NOT_FOUND.getCode());
            return OBJECT_MAPPER.writeValueAsString("Transfer not found");
        }
    }

    public Object makePlainTransfer(Request request, Response response) {
        int senderAccountId = Integer.valueOf(request.params("senderAccountId"));
        int receiverAccountId = Integer.valueOf(request.params("receiverAccountId"));
        BigDecimal sum = new BigDecimal(request.params("sum"));
        Transfer transfer = transferService.transferMoneyDirectly(senderAccountId,
                receiverAccountId, sum);
        return OBJECT_MAPPER.writeValueAsString(transfer);
    }

    public Object makePhoneTransfer(Request request, Response response) {
        String senderPhoneNumber = request.params("senderPhoneNumber");
        String receiverPhoneNumber = request.params("receiverPhoneNumber");
        BigDecimal sum = new BigDecimal(request.params("sum"));
        Transfer transfer = transferService.transferMoneyByPhoneNumber(senderPhoneNumber,
                receiverPhoneNumber, sum);
        return OBJECT_MAPPER.writeValueAsString(transfer);
    }

}

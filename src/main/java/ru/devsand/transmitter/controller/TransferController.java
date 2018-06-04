package ru.devsand.transmitter.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.j256.ormlite.support.ConnectionSource;
import ru.devsand.transmitter.controller.request.PhoneTransferRequest;
import ru.devsand.transmitter.controller.request.PlainTransferRequest;
import ru.devsand.transmitter.controller.request.TransferRequest;
import ru.devsand.transmitter.controller.response.ResponseStatus;
import ru.devsand.transmitter.controller.response.StandardResponse;
import ru.devsand.transmitter.model.entity.Account;
import ru.devsand.transmitter.model.entity.Transfer;
import ru.devsand.transmitter.model.repository.*;
import ru.devsand.transmitter.model.service.*;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.eclipse.jetty.http.HttpStatus.Code.NOT_FOUND;
import static ru.devsand.transmitter.controller.response.ResponseStatus.ERROR;
import static ru.devsand.transmitter.controller.response.ResponseStatus.SUCCESS;


public class TransferController {

    private final TransferService transferService;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public TransferController(ConnectionSource connectionSource) throws SQLException {
        TransferRepository transferRepository = new BasicTransferRepository(connectionSource);
        AccountRepository accountRepository = new BasicAccountRepository(connectionSource);
        CustomerRepository customerRepository = new BasicCustomerRepository(connectionSource);
        transferService = new BasicTransferService(transferRepository, accountRepository,
                customerRepository);
    }

    public Object getTransfer(Request request, Response response) throws SQLException {
        int id = Integer.valueOf(request.params(":id"));
        final Optional<Transfer> transferOp = transferService.getTransfer(id);
        if (transferOp.isPresent()) {
            return gson.toJson(StandardResponse.withData(transferOp.get()));
        } else {
            return gson.toJson(StandardResponse.errorResponse("Transfer not found"));
        }
    }

    public Object makePlainTransfer(Request request, Response response)  {
        PlainTransferRequest transferRequest = gson.fromJson(request.body(), PlainTransferRequest.class);
        if (transferRequest.isValid()) {
            try {
                Transfer transfer = transferService.transferMoneyDirectly(
                        transferRequest.getSenderAccountId(), transferRequest.getReceiverAccountId(),
                        transferRequest.getSum());
                return gson.toJson(StandardResponse.withData(transfer));
            } catch (AccountNotFoundException e) {
                return gson.toJson(StandardResponse.errorResponse("Account not found"));
            } catch (InsufficientFundsException e) {
                return gson.toJson(StandardResponse.errorResponse("Insufficient funds"));
            } catch (SQLException e) {
                return gson.toJson(StandardResponse.errorResponse("Database error"));
            }
        } else {
            return gson.toJson(StandardResponse.errorResponse("Invalid request"));
        }
    }


    public Object makePhoneTransfer(Request request, Response response)  {
        PhoneTransferRequest transferRequest = gson.fromJson(request.body(), PhoneTransferRequest.class);
        if (transferRequest.isValid()) {
            try {
                Transfer transfer = transferService.transferMoneyByPhoneNumber(transferRequest.getSenderPhoneNumber(),
                        transferRequest.getReceiverPhoneNumber(), transferRequest.getSum());
                return gson.toJson(StandardResponse.withData(transfer));
            } catch (CustomerNotFoundException e) {
                return gson.toJson(StandardResponse.errorResponse("Customer not found"));
            } catch (AccountNotFoundException e) {
                return gson.toJson(StandardResponse.errorResponse("Account not found"));
            } catch (InsufficientFundsException e) {
                return gson.toJson(StandardResponse.errorResponse("Insufficient funds"));
            } catch (SQLException e) {
                return gson.toJson(StandardResponse.errorResponse("Database error"));
            }
        } else {
            return gson.toJson(StandardResponse.errorResponse("Invalid request"));
        }
    }

    public Object handleNotFound(Request request, Response response) {
        return gson.toJson(StandardResponse.errorResponse("Function not found"));
    }

}

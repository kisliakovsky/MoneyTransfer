package ru.devsand.transmitter.rest;

import com.j256.ormlite.support.ConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.devsand.transmitter.controller.TransferController;
import ru.devsand.transmitter.model.connection.BasicDbConnector;
import ru.devsand.transmitter.model.connection.DbConnector;
import ru.devsand.transmitter.model.service.AccountNotFoundException;

import java.sql.SQLException;

import static ru.devsand.transmitter.model.DbInitializer.fillInDatabase;
import static spark.Spark.*;

public class BasicRestService implements RestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicRestService.class);

    private final int port;
    private final DbConnector<ConnectionSource> connector;
    private final TransferController transferController;

    public BasicRestService(int port) throws SQLException {
        this.port = port;
        this.connector = new BasicDbConnector();
        ConnectionSource connectionSource = connector.getConnectionSource();
        fillInDatabase(connectionSource);
        this.transferController = new TransferController(connectionSource);
    }

    @Override
    public void start() {
        port(port);
        path("/api", () -> {
            before("/*", (request, response) -> LOGGER.info("Received api call"));
            path("/transfer", () -> {
                get("/:id", transferController::getTransfer);
                post("/make", transferController::makePlainTransfer);
                post("/makebyphone", transferController::makePhoneTransfer);
            });
        });
        notFound(transferController::handleNotFound);
    }

    public void stop() {
        try {
            connector.close();
        } catch (Exception e) {
            LOGGER.error("Failed to close DB connection", e);
        }
    }

}

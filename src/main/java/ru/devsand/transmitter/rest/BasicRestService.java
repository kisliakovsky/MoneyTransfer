package ru.devsand.transmitter.rest;

import com.j256.ormlite.support.ConnectionSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.devsand.transmitter.controller.TransferController;
import ru.devsand.transmitter.model.connection.BasicDbConnector;
import ru.devsand.transmitter.model.connection.DbConnector;

import java.sql.SQLException;

import static ru.devsand.transmitter.model.DbInitializer.fillInDatabase;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class BasicRestService implements RestService {

    private static final Logger LOGGER = LogManager.getLogger(BasicRestService.class.getName());

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
        get("/transaction/:id", transferController::makePlainTransfer);
        post("/transaction", transferController::makePhoneTransfer);
    }

    public void stop() {
        try {
            connector.close();
        } catch (Exception e) {
            LOGGER.error("Failed to close DB connection", e);
        }
    }

}

package ru.devsand.transmitter.rest;

import com.j256.ormlite.support.ConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.devsand.transmitter.controller.TransferController;
import ru.devsand.transmitter.model.connection.BasicDbConnector;
import ru.devsand.transmitter.model.connection.DbConnector;
import spark.Spark;

import java.util.concurrent.TimeUnit;

import static ru.devsand.transmitter.model.DbInitializer.prepareDatabase;
import static spark.Spark.*;

public class BasicRestService implements RestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicRestService.class);

    private final int port;
    private final DbConnector<ConnectionSource> connector;
    private final TransferController transferController;

    public BasicRestService(int port)  {
        this.port = port;
        try {
            this.connector = new BasicDbConnector();
            ConnectionSource connectionSource = connector.getConnectionSource();
            prepareDatabase(connectionSource);
            this.transferController = new TransferController(connectionSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        port(port);
        path("/api", () -> {
            before("/*", (request, response) -> LOGGER.info("Received api call"));
            get("/transfers", transferController::getTransfers);
            path("/transfers", () -> {
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
        Spark.stop();
        blockUntilStop();
    }

    private void blockUntilStop() {
        while (true) {
            try {
                Spark.port(); // throws IllegalStateException when server has stoped
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (IllegalStateException ignored) {
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

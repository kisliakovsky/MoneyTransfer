package ru.devsand.transmitter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.devsand.transmitter.rest.BasicRestService;
import ru.devsand.transmitter.rest.RestService;
import ru.devsand.transmitter.model.DbInitializer;

import java.sql.SQLException;

public class Launcher {

    private static final Logger LOGGER = LogManager.getLogger(Launcher.class.getName());

    public static void main(String[] args) {
        try {
            LOGGER.debug("starting rest service");
            RestService restService = new BasicRestService(4567);
            restService.start();
        } catch (SQLException e) {
            LOGGER.error("Database error", e);
        }
    }

}

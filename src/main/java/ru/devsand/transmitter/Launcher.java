package ru.devsand.transmitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.devsand.transmitter.rest.BasicRestService;
import ru.devsand.transmitter.rest.RestService;

import java.sql.SQLException;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class.getName());

    public static void main(String[] args) {
        try {
            RestService restService = new BasicRestService(4567);
            restService.start();
        } catch (SQLException e) {
            LOGGER.error("database error", e);
        }
    }

}

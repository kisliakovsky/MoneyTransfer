package ru.devsand.transmitter.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.StringJoiner;

import static org.hamcrest.MatcherAssert.assertThat;


public class BasicRestServiceTest {

    private static String PATH_SEPARATOR = "/";
    private static int PORT = 8080;
    private static String HOST_ADDRESS = String.format("localhost:%d", PORT);

    private StringJoiner pathJoiner;
    private RestService restService;

    @Before
    public void setUpTest() throws SQLException {
        pathJoiner = new StringJoiner(PATH_SEPARATOR);
        pathJoiner.add(HOST_ADDRESS).add("api").add("transfers");
        restService = new BasicRestService(PORT);
        restService.start();
    }

    @After
    public void tearDown() {
        restService.stop();
    }

    @Test
    public void checkUrl() {
        System.out.println(pathJoiner.toString());
    }

}
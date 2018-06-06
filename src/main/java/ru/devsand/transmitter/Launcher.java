package ru.devsand.transmitter;

import ru.devsand.transmitter.rest.BasicRestService;
import ru.devsand.transmitter.rest.RestService;

public class Launcher {

    public static void main(String[] args) {
        try {
            RestService restService = new BasicRestService(4567);
            restService.start();
        } catch (Exception e) {
            System.err.println("Unable to start REST service");
        }
    }

}

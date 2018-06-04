package ru.devsand.transmitter.controller.response;

public enum ResponseStatus {

    SUCCESS("Success"),
    ERROR("Error");

    private String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}

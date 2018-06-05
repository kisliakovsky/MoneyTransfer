package ru.devsand.transmitter.rest;

import ru.devsand.transmitter.model.entity.Transfer;

public class TransferResponse {

    private String status;
    private String message;
    private Transfer data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Transfer getData() {
        return data;
    }

    public void setData(Transfer data) {
        this.data = data;
    }
}

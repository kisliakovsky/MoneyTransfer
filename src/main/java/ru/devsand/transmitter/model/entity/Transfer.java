package ru.devsand.transmitter.model.entity;

import java.time.LocalDateTime;

public class Transfer {

    private long id;
    private long senderAccountId;
    private long receiverAccountId;
    private LocalDateTime timeStamp;

    public Transfer(long senderAccountId, long receiverAccountId) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.timeStamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public long getSenderAccountId() {
        return senderAccountId;
    }

    public long getReceiverAccountId() {
        return receiverAccountId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

}

package ru.devsand.transmitter.rest;

import java.math.BigDecimal;

public class PlainRequest {

    private long senderAccountId;
    private long receiverAccountId;
    private BigDecimal sum;

    public PlainRequest(long senderAccountId, long receiverAccountId, BigDecimal sum) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.sum = sum;
    }

    public long getSenderAccountId() {
        return senderAccountId;
    }

    public long getReceiverAccountId() {
        return receiverAccountId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSenderAccountId(long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public void setReceiverAccountId(long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

}

package ru.devsand.transmitter.rest;

import java.math.BigDecimal;

public class PhoneRequest {

    private String senderPhoneNumber;
    private String receiverPhoneNumber;
    private BigDecimal sum;

    public PhoneRequest(String senderPhoneNumber, String receiverPhoneNumber, BigDecimal sum) {
        this.senderPhoneNumber = senderPhoneNumber;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.sum = sum;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}

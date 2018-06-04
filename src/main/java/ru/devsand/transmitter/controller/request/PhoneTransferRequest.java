package ru.devsand.transmitter.controller.request;

import java.math.BigDecimal;

public class PhoneTransferRequest implements TransferRequest {

    private String senderPhoneNumber;
    private String receiverPhoneNumber;
    private BigDecimal sum;

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

    @Override
    public boolean isValid() {
        return senderPhoneNumber != null && receiverPhoneNumber != null &&
                sum.compareTo(BigDecimal.ZERO) > 0;
    }
}

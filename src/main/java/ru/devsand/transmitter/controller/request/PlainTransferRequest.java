package ru.devsand.transmitter.controller.request;

import java.math.BigDecimal;

public class PlainTransferRequest implements TransferRequest {

    private Long senderAccountId;
    private Long receiverAccountId;
    private BigDecimal sum;

    public long getSenderAccountId() {
        return senderAccountId;
    }

    public boolean isValid() {
        return senderAccountId != null && receiverAccountId != null &&
                sum.compareTo(BigDecimal.ZERO) > 0;
    }

    public void setSenderAccountId(long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}

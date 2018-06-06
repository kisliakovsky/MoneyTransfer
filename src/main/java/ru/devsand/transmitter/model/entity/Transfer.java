package ru.devsand.transmitter.model.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.sql.Timestamp;


@DatabaseTable(tableName = "transfers")
public class Transfer implements Entity {

    @DatabaseField(generatedId = true, useGetSet = true)
    private long id;
    @DatabaseField(columnName = "sender_id", canBeNull = false, foreign = true, useGetSet = true)
    private Account senderAccount;
    @DatabaseField(columnName = "receiver_id", canBeNull = false, foreign = true, useGetSet = true)
    private Account receiverAccount;
    @DatabaseField(columnName = "sum", canBeNull = false, useGetSet = true)
    private BigDecimal sum;
    @DatabaseField(columnName = "time_stamp", canBeNull = false, dataType = DataType.TIME_STAMP, useGetSet = true)
    private Timestamp timestamp;

    public Transfer() {
    }

    public Transfer(Account senderAccount, Account receiverAccount, BigDecimal sum,
                    Timestamp timestamp) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.sum = sum;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

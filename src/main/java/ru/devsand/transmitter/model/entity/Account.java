package ru.devsand.transmitter.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

@DatabaseTable(tableName = "accounts")
public class Account implements Entity {

    @DatabaseField(generatedId = true, useGetSet = true)
    private long id;
    @DatabaseField(columnName = "customer_id", canBeNull = false, foreign = true, useGetSet = true)
    private Customer customer;
    @DatabaseField(columnName = "balance", canBeNull = false, useGetSet = true)
    private BigDecimal balance;
    @DatabaseField(columnName = "priority", canBeNull = false, useGetSet = true)
    private int priority;
    @DatabaseField(columnName = "currency", canBeNull = false, width = 3, useGetSet = true)
    private String currency;

    public Account() {
    }

    public Account(Customer customer, BigDecimal balance, int priority, String currency) {
        this.customer = customer;
        this.balance = balance;
        this.priority = priority;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}

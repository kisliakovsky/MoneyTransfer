package ru.devsand.transmitter.model.entity;

import java.math.BigDecimal;

public class Account {

    private long id;
    private Customer customer;
    private BigDecimal balance;
    private int priority;
    private String currency;

    public Account(Customer customer, BigDecimal balance, int priority, String currency) {
        this.customer = customer;
        this.balance = balance;
        this.priority = priority;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getPriority() {
        return priority;
    }

    public String getCurrency() {
        return currency;
    }

}

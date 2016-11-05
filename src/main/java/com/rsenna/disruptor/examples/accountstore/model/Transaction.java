package com.rsenna.disruptor.examples.accountstore.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Example transaction, simple for demo only, sign of amount determines 
 * directionality when applied to balances. In the real world, we might
 * choose to enforce immutability on such objects.
 */
public class Transaction {
    private Date date;
    private BigDecimal amount;
    private String accountNumber;
    private String type;

    public Transaction(Date date, BigDecimal amount, String accountNumber, String type) {
        this.date = date;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s] %s %s", date, type, accountNumber, amount);
    }
}

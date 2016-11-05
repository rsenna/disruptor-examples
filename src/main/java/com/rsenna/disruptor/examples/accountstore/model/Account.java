package com.rsenna.disruptor.examples.accountstore.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Example account, for demo purposes.
 */
public class Account {
    private String accountNumber;
    private List<Transaction> history = new ArrayList<>();
    private BigDecimal balance = new BigDecimal(0);

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void post(Transaction transaction) {
        balance = balance.add(transaction.getAmount());
        history.add(transaction);
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Account: %s -- Balance: %s %n", accountNumber, balance));
        builder.append("Transactions: " + System.lineSeparator());
        for (Transaction t: history) {
            builder.append(t.toString());
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}

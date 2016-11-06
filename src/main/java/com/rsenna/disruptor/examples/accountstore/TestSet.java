package com.rsenna.disruptor.examples.accountstore;

import com.rsenna.disruptor.examples.accountstore.transaction.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rsenna on 2016-11-06.
 */
public class TestSet {
    private TestSet() {}

    static final List<Transaction> A = new ArrayList<>();
    static final List<Transaction> B = new ArrayList<>();

    static {
        A.add(new Transaction(new Date(), new BigDecimal("1200.00"), "54321", "New Account Opened"));
        A.add(new Transaction(new Date(), new BigDecimal("-134.56"), "54321", "Check Cleared"));
        A.add(new Transaction(new Date(), new BigDecimal("129.78"), "12345", "Direct Deposit"));
        A.add(new Transaction(new Date(), new BigDecimal("100.00"), "54321", "Deposit"));
        A.add(new Transaction(new Date(), new BigDecimal("-60"), "12345", "ATM Withdrawal"));
        A.add(new Transaction(new Date(), new BigDecimal("125.56"), "12345", "Direct Deposit"));
        A.add(new Transaction(new Date(), new BigDecimal("-568.90"), "54321", "Loan Payment"));
    }

    static {
        B.add(new Transaction(new Date(), new BigDecimal("2478.45"), "66611", "Direct Deposit"));
        B.add(new Transaction(new Date(), new BigDecimal("-234.60"), "66611", "Check Cleared"));
        B.add(new Transaction(new Date(), new BigDecimal("400.34"), "12345", "Deposit"));
        B.add(new Transaction(new Date(), new BigDecimal("95.50"), "54321", "Deposit"));
        B.add(new Transaction(new Date(), new BigDecimal("-50"), "66611", "ATM Withdrawal"));
        B.add(new Transaction(new Date(), new BigDecimal("-10.00"), "12345", "Service Fee"));
        B.add(new Transaction(new Date(), new BigDecimal("-10.00"), "54321", "Service Fee"));
    }
}

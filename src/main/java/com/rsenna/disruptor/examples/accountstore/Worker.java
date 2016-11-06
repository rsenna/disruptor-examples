package com.rsenna.disruptor.examples.accountstore;

import com.rsenna.disruptor.examples.accountstore.transaction.Transaction;
import com.rsenna.disruptor.examples.accountstore.transaction.TransactionEventPublisher;
import com.rsenna.disruptor.examples.accountstore.transaction.TransactionProcessor;

import java.util.List;

/**
 * Created by rsenna on 2016-11-06.
 */
public class Worker implements Runnable {
    private TransactionProcessor tp;
    private List<Transaction> testSet;

    public Worker(List<Transaction> testSet, TransactionProcessor tp) {
        this.tp = tp;
        this.testSet = testSet;
    }

    @Override
    public void run() {
        for (Transaction tx : testSet) {
            tp.getRingBuffer().publishEvent(new TransactionEventPublisher(tx));
        }
    }
}

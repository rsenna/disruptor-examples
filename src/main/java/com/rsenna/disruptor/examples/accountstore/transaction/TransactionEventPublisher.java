package com.rsenna.disruptor.examples.accountstore.transaction;

import com.lmax.disruptor.EventTranslator;

/**
 * This is the way events get into the system - the ring buffer is full of pre-allocated events
 * and this is how a specific transaction's state is input to the buffer. Pre-allocation of the buffer
 * is a key component of the Disruptor pattern.
 */
public class TransactionEventPublisher implements EventTranslator<TransactionEvent> {
    private Transaction transaction;

    public TransactionEventPublisher(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void translateTo(TransactionEvent event, long sequence) {
        event.setTransaction(transaction);
        event.setBufferSeq(sequence); // We don't really use this, just demonstrating its availability
    }
}

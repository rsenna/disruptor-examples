package com.rsenna.disruptor.examples.accountstore.transaction;

import com.lmax.disruptor.EventFactory;
import lombok.Data;

/**
 * The TransactionEvent is at the core of the pattern - this is the data structure
 * with which the ring-buffer works, and represents the data for our transaction-sourcing
 * implementation.
 */
@Data
public class TransactionEvent {
    private Transaction transaction;
    private long bufferSeq = 0;

    /**
     * EventFactory is specified by the disruptor framework. This is how the ring-buffer populates itself.
     * See init() in TransactionProcessor.
     */
    public static final EventFactory<TransactionEvent> EVENT_FACTORY = TransactionEvent::new;

    /**
     * Would this go here in the real world? Maybe, maybe not.
     */
    public String asJournalEntry() {
        return String.format("%s|%s|%s|%s|%s%n", this.getBufferSeq(),
                this.getTransaction().getDate().getTime(),
                this.getTransaction().getAccountNumber(),
                this.getTransaction().getType(),
                this.getTransaction().getAmount());
    }
}

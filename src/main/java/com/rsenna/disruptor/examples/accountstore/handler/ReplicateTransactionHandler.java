package com.rsenna.disruptor.examples.accountstore.handler;

import com.lmax.disruptor.EventHandler;
import com.rsenna.disruptor.examples.accountstore.transaction.TransactionEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * In the real world, this handler would replicate the transaction transaction to in-memory
 * date stores running on one or more other nodes as part of a redundancy strategy.
 */
@Slf4j
public class ReplicateTransactionHandler implements EventHandler<TransactionEvent> {
    @Override
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.warn("TODO: REPLICATE -> {}", event.getTransaction().toString());
    }
}

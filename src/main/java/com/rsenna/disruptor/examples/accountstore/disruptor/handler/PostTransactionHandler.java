package com.rsenna.disruptor.examples.accountstore.disruptor.handler;

import com.lmax.disruptor.EventHandler;
import com.rsenna.disruptor.examples.accountstore.AccountStore;
import com.rsenna.disruptor.examples.accountstore.disruptor.event.TransactionEvent;
import com.rsenna.disruptor.examples.accountstore.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler uses the event to update the in-memory data store. Operations on 
 * this store always happen in a single thread, so concurrency issues are non-
 * existent for these updates. 
 */
public class PostTransactionHandler implements EventHandler<TransactionEvent> {
    private static final Logger logger = LoggerFactory.getLogger(PostTransactionHandler.class);
    private AccountStore accountStore;

    public PostTransactionHandler(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    @Override
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws Exception {
        final String accountNumber = event.getTransaction().getAccountNumber();
        final Account act = accountStore.getAccount(accountNumber)
                .orElse(new Account(accountNumber));

        act.post(event.getTransaction());
        accountStore.saveAccount(act);

        logger.debug("POSTED TRANSACTION -> {}", event.getTransaction().toString());
    }
}
package com.rsenna.disruptor.examples.accountstore.handler;

import com.lmax.disruptor.EventHandler;
import com.rsenna.disruptor.examples.accountstore.account.AccountStore;
import com.rsenna.disruptor.examples.accountstore.transaction.TransactionEvent;
import com.rsenna.disruptor.examples.accountstore.account.Account;
import lombok.extern.slf4j.Slf4j;

/**
 * This handler uses the transaction to update the in-memory data store. Operations on
 * this store always happen in a single thread, so concurrency issues are non-
 * existent for these updates.
 */
@Slf4j
public class PostTransactionHandler implements EventHandler<TransactionEvent> {
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

        log.debug("POSTED TRANSACTION -> {}", event.getTransaction().toString());
    }
}

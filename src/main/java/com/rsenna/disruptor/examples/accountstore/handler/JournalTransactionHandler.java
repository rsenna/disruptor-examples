package com.rsenna.disruptor.examples.accountstore.handler;

import com.lmax.disruptor.EventHandler;
import com.rsenna.disruptor.examples.accountstore.transaction.TransactionEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * In the real world, this handler would write the transaction transaction to a durable journal, such
 * that these events can be re-played after a system failure, to rebuild the state of the
 * in memory store. In this example handler, we just do a simplistic file write as a stand-in
 * for a more sophisticated approach.
 */
@Slf4j
public class JournalTransactionHandler implements EventHandler<TransactionEvent>, Closeable {
    private FileWriter journal;

    public JournalTransactionHandler(File journalFile) {
        try {
            this.journal = new FileWriter(journalFile, true);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws Exception {
        journal.write(event.asJournalEntry());
        journal.flush();
        log.debug("JOURNALED TRANSACTION -> {}", event.getTransaction().toString());
    }

    @Override
    public void close() throws IOException {
        if (journal != null) {
            journal.flush();
            journal.close();
        }
    }
}

package com.rsenna.disruptor.examples.accountstore.transaction;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.rsenna.disruptor.examples.accountstore.account.AccountStore;
import com.rsenna.disruptor.examples.accountstore.handler.GenericExceptionHandler;
import com.rsenna.disruptor.examples.accountstore.handler.JournalTransactionHandler;
import com.rsenna.disruptor.examples.accountstore.handler.PostTransactionHandler;
import com.rsenna.disruptor.examples.accountstore.handler.ReplicateTransactionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <p>Prototype based on the LMAX disruptor framework:
 *       http://lmax-exchange.github.io/disruptor/ </p>
 *
 * <p>Inspired by this blog post:
 *       http://blog.jteam.nl/2011/07/20/processing-1m-tps-with-axon-framework-and-the-disruptor/ </p>
 *
 * <p>This is an extremely oversimplified version of the "diamond configuration"
 * as described in http://mechanitis.blogspot.com/2011/07/dissecting-disruptor-wiring-up.html </p>
 *
 * <p>In this implementation, a journal and replication step happen concurrently,
 * and both must succeed for the post step to occur, as shown below: </p>
 *
 * <pre>
 *          replicate
 *            /   \
 *           /     \
 *  transaction ->       post
 *           \     /
 *            \   /
 *           journal
 * </pre>
 *
 * <p>This is also an example of in-memory storage based on "transaction sourcing" -
 * see Martin Fowler: http://martinfowler.com/eaaDev/EventSourcing.html</p>
 *
 */
@Slf4j
public class TransactionProcessor implements AutoCloseable {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);

    private Disruptor<TransactionEvent> disruptor;
    private RingBuffer<TransactionEvent> ringBuffer;
    private AccountStore accountStore;

    private JournalTransactionHandler journal;
    private ReplicateTransactionHandler replicate;
    private PostTransactionHandler post;

    public TransactionProcessor(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        disruptor = new Disruptor<>(
                TransactionEvent.EVENT_FACTORY,
                1024,
                EXECUTOR,
                ProducerType.SINGLE,
                new YieldingWaitStrategy());

        // Pretend that we have real journaling, just to demo it...
        File journalDir = new File("target/test");
        journalDir.mkdirs();
        File journalFile = new File(journalDir, "test-journal.txt");

        // In this example start fresh each time - though a real implementation
        // might roll over the journal or the like.
        if (journalFile.exists()) {
            journalFile.delete();
        }

        journal = new JournalTransactionHandler(journalFile);
        replicate = new ReplicateTransactionHandler();
        post = new PostTransactionHandler(accountStore);

        // This is where the magic happens
        // (see "diamond configuration" in javadoc above)
        disruptor.handleEventsWith(journal, replicate).then(post);

        // We don't do any fancy exception handling in this demo, but if we
        // did, one way to set it up for each handler is like this:
        GenericExceptionHandler exh = new GenericExceptionHandler();
        disruptor.handleExceptionsFor(journal).with(exh);
        disruptor.handleExceptionsFor(replicate).with(exh);
        disruptor.handleExceptionsFor(post).with(exh);

        ringBuffer = disruptor.start();
    }

    public RingBuffer<TransactionEvent> getRingBuffer() {
        return ringBuffer;
    }

    @Override
    public void close() {
        try {
            journal.close();
        } catch (Exception ignored) {
            log.error(ignored.getMessage(), ignored);
        }

        try {
            disruptor.shutdown();
        } catch (Exception ignored) {
            log.error(ignored.getMessage(), ignored);
        }

        EXECUTOR.shutdownNow();
    }
}

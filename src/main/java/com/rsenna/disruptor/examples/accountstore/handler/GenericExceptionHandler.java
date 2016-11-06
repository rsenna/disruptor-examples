package com.rsenna.disruptor.examples.accountstore.handler;

import com.lmax.disruptor.ExceptionHandler;
import com.rsenna.disruptor.examples.accountstore.transaction.TransactionEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Any un-handled or thrown exception in processing by an transaction handler will
 * be reported through an implementation of ExceptionHandler. Depending upon
 * which step in our "diamond configuration" has failed, we would take
 * action. For example, if posting failed after journaling and replication,
 * we might issue compensating journal and replication events.
 */
@Slf4j
public class GenericExceptionHandler implements ExceptionHandler<TransactionEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, TransactionEvent event) {
        log.error("Caught unhandled exception while processing: " + event, ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        log.error("Unexpected exception during startup.", ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error("Unexpected exception during shutdown.", ex);
    }
}

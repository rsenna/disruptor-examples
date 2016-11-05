package com.rsenna.disruptor.examples.accountstore.handler;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Any un-handled or thrown exception in processing by an event handler will
 * be reported through an implementation of ExceptionHandler. Depending upon
 * which step in our "diamond configuration" has failed, we would take
 * action. For example, if posting failed after journaling and replication,
 * we might issue compensating journal and replication events.
 */
@Slf4j
public class GenericExceptionHandler implements ExceptionHandler<Object> {
    public void handleEventException(Throwable ex, long sequence, Object event) {
        log.error("Caught unhandled exception while processing: "+event.toString(), ex);
    }

    public void handleOnStartException(Throwable ex) {
        log.error("Unexpected exception during startup.", ex);
    }

    public void handleOnShutdownException(Throwable ex) {
        log.error("Unexpected exception during shutdown.", ex);
    }
}

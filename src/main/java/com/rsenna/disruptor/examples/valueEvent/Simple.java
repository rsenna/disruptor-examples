package com.rsenna.disruptor.examples.valueevent;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class Simple {
    private Simple() {}

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        final ThreadFactory threadFactory = Executors.defaultThreadFactory();

        // Preallocate RingBuffer with 1024 ValueEvents
        final Disruptor<ValueEvent> disruptor = new Disruptor<>(ValueEvent::new, 1024, threadFactory);
        final EventHandler<ValueEvent> handler = (event, sequence, endOfBatch) -> {
            log.info("Sequence: " + sequence);
            log.info("ValueEvent: " + event.getValue());
        };

        // Build dependency graph
        disruptor.handleEventsWith(handler);

        final RingBuffer<ValueEvent> ringBuffer = disruptor.start();

        for (long i = 10; i < 2000; i++) {
            final String uuid = UUID.randomUUID().toString();

            // Two phase commit. Grab one of the 1024 slots
            final long seq = ringBuffer.next();
            final ValueEvent valueEvent = ringBuffer.get(seq);

            valueEvent.setValue(uuid);
            ringBuffer.publish(seq);
        }

        disruptor.shutdown();
    }
}

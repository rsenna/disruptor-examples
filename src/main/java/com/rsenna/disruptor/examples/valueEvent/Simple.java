package com.rsenna.disruptor.examples.valueevent;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Simple {
    private Simple() {}

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        // Preallocate RingBuffer with 1024 ValueEvents
        Disruptor<ValueEvent> disruptor = new Disruptor<>(ValueEvent.EVENT_FACTORY, 1024, exec);
        final EventHandler<ValueEvent> handler = (event, sequence, endOfBatch) -> {
            log.info("Sequence: " + sequence);
            log.info("ValueEvent: " + event.getValue());
        };
        // Build dependency graph
        disruptor.handleEventsWith(handler);
        RingBuffer<ValueEvent> ringBuffer = disruptor.start();

        for (long i = 10; i < 2000; i++) {
            String uuid = UUID.randomUUID().toString();
            // Two phase commit. Grab one of the 1024 slots
            long seq = ringBuffer.next();
            ValueEvent valueEvent = ringBuffer.get(seq);
            valueEvent.setValue(uuid);
            ringBuffer.publish(seq);
        }
        disruptor.shutdown();
        exec.shutdown();
    }
}

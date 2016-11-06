package com.rsenna.disruptor.examples.valueevent;

import com.lmax.disruptor.EventFactory;
import lombok.Data;

/**
 * WARNING: This is a mutable object which will be recycled by the RingBuffer. You must take a copy of data it holds
 * before the framework recycles it.
 */
@Data
public final class ValueEvent {
    private String value;

}

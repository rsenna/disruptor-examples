package com.rsenna.disruptor.examples.accountstore.transaction;

import lombok.Value;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Example transaction, simple for demo only, sign of amount determines
 * directionality when applied to balances. In the real world, we might
 * choose to enforce immutability on such objects.
 */
@Value
public class Transaction {
    Date date;
    BigDecimal amount;
    String accountNumber;
    String type;
}

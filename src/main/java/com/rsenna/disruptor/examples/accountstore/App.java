package com.rsenna.disruptor.examples.accountstore;

import com.rsenna.disruptor.examples.accountstore.account.AccountStore;
import com.rsenna.disruptor.examples.accountstore.transaction.TransactionProcessor;

import java.io.File;
import java.io.IOException;

/**
 * Based on http://www.wjblackburn.me/resources/LMAX-disruptor-example.html
 */
public class App {
    private App() {}

    public static void main(String[] args) throws Exception {
        multiThreadUpdatesTest();
        restoreFromJournalTest();
    }

    public static void multiThreadUpdatesTest() throws InterruptedException {
        AccountStore accounts = new AccountStore();

        try (TransactionProcessor processor = new TransactionProcessor(accounts)) {
            processor.init();

            Thread t1 = new Thread(new Worker(TestSet.A, processor));
            Thread t2 = new Thread(new Worker(TestSet.B, processor));

            t1.start();
            t2.start();

            // Wait for the transactions to filter through, of course you would
            // usually have the transaction processor lifecycle managed by a
            // container or in some other more sophisticated way...
            Thread.sleep(3000);
        }
        finally {
            accounts.dumpAccounts();
        }
    }

    public static void restoreFromJournalTest() throws IOException {
        AccountStore accounts = new AccountStore();

        accounts.restoreFromJournal(new File("target/test/test-journal.txt"));
        accounts.dumpAccounts();
    }
}

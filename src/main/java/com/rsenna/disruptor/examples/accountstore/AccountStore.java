package com.rsenna.disruptor.examples.accountstore;

import com.rsenna.disruptor.examples.accountstore.model.Account;
import com.rsenna.disruptor.examples.accountstore.model.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A quick and dirty stand-in for a sophisticated in-memory data store. Note
 * that this store is in no way thread safe, but when accessed via the
 * TransactionProcessor disruptor, it will only ever be altered by a single
 * thread anyway! This is the beauty of the disruptor.
 */
@Slf4j
public class AccountStore {

    Map<String, Account> store = new HashMap<>();

    public Optional<Account> getAccount(String accountNumber) {
        return Optional.ofNullable(store.get(accountNumber));
    }

    public void saveAccount(Account account) {
        store.put(account.getAccountNumber(), account);
    }

    /**
     * This is probably not the right place to put this method - but I wanted
     * to validate the example journal somehow, and this seemed easiest...
     *
     * @param journalFile
     */
    public void restoreFromJournal(File journalFile) throws IOException {
        if (!journalFile.exists()) {
            return;
        }

        try (final FileReader fileReader = new FileReader(journalFile);
             final BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line)) {
                    String[] attrs = line.split("\\|");
                    // 0 - sequence (long)
                    // 1 - date     (long)
                    // 2 - account  (String)
                    // 3 - type     (String)
                    // 4 - amount   (BigDecimal)
                    final Date date = new Date(Long.parseLong(attrs[1]));
                    final BigDecimal amount = new BigDecimal(attrs[4]);
                    Transaction t = new Transaction(date, amount, attrs[2], attrs[3]);
                    Account act = getAccount(t.getAccountNumber())
                            .orElse(new Account(t.getAccountNumber()));

                    act.post(t);
                    saveAccount(act);
                }
            }
        }
    }

    public void dumpAccounts() {
        for (Account account : store.values()) {
            log.info(account.toString() + "==============================================================");
        }
    }
}


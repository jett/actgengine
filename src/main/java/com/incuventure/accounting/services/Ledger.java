package com.incuventure.accounting.services;

import com.incuventure.accounting.AccountingEntry;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class Ledger {

    List<AccountingEntry> entries;

    public void addEntry(String reference, String action, String unitCode, String bookCode, String code, String accountDescription, String currency, BigInteger amount, String description) {

        if(entries == null) {
            entries = new ArrayList<AccountingEntry>();
        }

        entries.add(new AccountingEntry(reference, action, unitCode,  bookCode, code, accountDescription, currency, amount, description));

    }

    public List<AccountingEntry> getEntries() {
        return entries;
    }
}

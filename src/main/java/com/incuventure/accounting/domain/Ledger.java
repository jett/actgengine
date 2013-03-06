package com.incuventure.accounting.domain;

import com.incuventure.accounting.AccountingEntry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class Ledger {

    List<AccountingEntry> entries;

    public void addEntry(AccountingEntryType type, String reference, String action, String unitCode, String bookCode, String code, String accountDescription, String currency, BigDecimal amount, String description) {

        if(entries == null) {
            entries = new ArrayList<AccountingEntry>();
        }

        entries.add(new AccountingEntry(type, reference, action, unitCode,  bookCode, code, accountDescription, currency, amount, description));

    }

    public List<AccountingEntry> getEntriesForReference(String reference) {

        List<AccountingEntry> results = new ArrayList<AccountingEntry>();

        for(AccountingEntry entry : entries) {

            if(entry.getReference().equalsIgnoreCase(reference)) {
                results.add(entry);
            }
        }

        return results;

    }

    public List<AccountingEntry> getEntries() {
        return entries;
    }

    public List<AccountingEntry> getEntriesOfType(AccountingEntryType entryType) {

        List<AccountingEntry> results = new ArrayList<AccountingEntry>();

        for(AccountingEntry entry : entries) {

            if(entry.getAccountingEntryType() == entryType) {
                results.add(entry);
            }
        }

        return results;
    }

    public List<AccountingEntry> getEntriesOfTypeAndCode(AccountingEntryType entryType, String code) {

        List<AccountingEntry> results = new ArrayList<AccountingEntry>();

        for(AccountingEntry entry : entries) {

            if((entry.getAccountingEntryType() == entryType) && (entry.getCode().equalsIgnoreCase(code))) {
                results.add(entry);
            }
        }

        return results;
    }
}
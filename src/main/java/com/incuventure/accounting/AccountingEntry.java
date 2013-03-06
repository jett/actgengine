package com.incuventure.accounting;

import com.incuventure.accounting.domain.AccountingEntryType;

import java.math.BigDecimal;

public class AccountingEntry {

    String reference;
    String action;
    String unitCode;
    String bookCode;
    String currency;
    String accountDescription;
    BigDecimal amount;
    String description;
    String code;
    AccountingEntryType accountingEntryType;

    public AccountingEntry(AccountingEntryType entryType, String reference, String action, String unitCode, String bookCode, String code, String accountDescription, String currency, BigDecimal amount, String description) {
        this.reference = reference;
        this.action = action;
        this.unitCode = unitCode;
        this.bookCode = bookCode;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.code = code;
        this.accountDescription = accountDescription;
        this.accountingEntryType = entryType;
    }

    public AccountingEntry(String reference, String action, String unitCode, String bookCode, String code, String accountDescription, String currency, BigDecimal amount, String description) {
        this.reference = reference;
        this.action = action;
        this.unitCode = unitCode;
        this.bookCode = bookCode;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.code = code;
        this.accountDescription = accountDescription;
    }

    @Override
    public String toString() {
        return "ref:" + this.reference + "\t " + (this.action.equalsIgnoreCase("C") ? "CR" : "DR") +" \t" + this.unitCode +" \t" + this.bookCode +  "\t" + this.code + "\t" + this.accountDescription + "\t" + this.currency + "\t" + this.amount + "\t" + (this.description!=null?this.description:"");
    }

    public String getReference() {
        return reference;
    }

    public String getAction() {
        return action;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getBookCode() {
        return bookCode;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public AccountingEntryType getAccountingEntryType() {
        return accountingEntryType;
    }
}

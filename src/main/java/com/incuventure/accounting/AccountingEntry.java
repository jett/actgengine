package com.incuventure.accounting;

import java.math.BigInteger;

public class AccountingEntry {

    String reference;
    String action;
    String unitCode;
    String bookCode;
    String currency;
    String accountDescription;
    BigInteger amount;
    String description;
    String code;

    public AccountingEntry(String reference, String action, String unitCode, String bookCode, String code, String accountDescription, String currency, BigInteger amount, String description) {
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

        return "ref:" + this.reference + "\t CR: " + this.unitCode +"\t" + this.bookCode +  "\t" + this.code + "\t" + this.accountDescription + "\t" + this.currency + "\t" + this.amount + "\t" + this.description;

    }
}

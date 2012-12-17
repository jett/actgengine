package com.incuventure.accounting.event.product;


import java.math.BigInteger;

public class ContingentLiabilityReleasedEvent {

    String productCode;
    String currency;
    BigInteger amount;
    String reference;

    public ContingentLiabilityReleasedEvent(String productCode, String currency, BigInteger amount, String reference) {
        this.productCode = productCode;
        this.currency = currency;
        this.amount = amount;
        this.reference = reference;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getCurrency() {
        return currency;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public String getReference() {
        return reference;
    }
}

package com.incuventure.accounting.event.product;


import com.incuventure.ddd.domain.DomainEvent;

import java.math.BigInteger;

public class ContingentLiabilityIncurredEvent implements DomainEvent {

    String productCode;
    String currency;
    BigInteger amount;
    String reference;
    String description;  // description of the transaction (needed for some)

    public String getDescription() {
        return description;
    }

    public ContingentLiabilityIncurredEvent(String reference, String productCode, String currency, BigInteger amount, String description) {
        this.productCode = productCode;
        this.currency = currency;
        this.amount = amount;
        this.reference = reference;
        this.description = description;

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

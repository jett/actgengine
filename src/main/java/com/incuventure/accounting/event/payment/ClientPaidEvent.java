package com.incuventure.accounting.event.payment;

import com.incuventure.ddd.domain.DomainEvent;

import java.math.BigDecimal;

public class ClientPaidEvent implements DomainEvent {

    String paymentMode;
    String currency;
    BigDecimal amount;
    String unitCode;
    String reference;
    String description;  // description of the transaction (needed for some)

    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    public ClientPaidEvent(String reference, String paymentMode, String currency, BigDecimal amount, String unitCode) {
        this.paymentMode = paymentMode;
        this.currency = currency;
        this.amount = amount;
        this.unitCode = unitCode;
        this.reference = reference;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUnitCode() {
        return unitCode;
    }
}

package com.incuventure.accounting.event.charge;

import com.incuventure.ddd.domain.DomainEvent;

import java.math.BigInteger;


public class ClientChargedEvent implements DomainEvent {

    String unitCode;     // unit code if needs to be overridden
    String glCode;       // GL code if needs to be overridden
    String chargeCode;   // required: code for the charge fired
    String reference;    // reference to the transaction
    String description;  // description of the transaction (needed for some)
    String product;
    BigInteger amount;

    public ClientChargedEvent(String reference, String chargeCode, String product, BigInteger amount, String unitCode, String description) {
        this.unitCode = unitCode;
        this.description = description;
        this.chargeCode = chargeCode;
        this.reference = reference;
        this.amount = amount;
        this.product = product;
    }

    public ClientChargedEvent(String reference, String chargeCode, String product, BigInteger amount, String unitCode) {
        this.unitCode = unitCode;
        this.chargeCode = chargeCode;
        this.reference = reference;
        this.amount = amount;
        this.product = product;
    }

    public ClientChargedEvent(String reference, String chargeCode, String product, BigInteger amount, String unitCode, String glCode, String description) {
        this.unitCode = unitCode;
        this.description = description;
        this.chargeCode = chargeCode;
        this.glCode = glCode;
        this.reference = reference;
        this.amount = amount;
        this.product = product;
    }

    public ClientChargedEvent(String reference, String chargeCode, String product, BigInteger amount) {
        this.chargeCode = chargeCode;
        this.description = "";
        this.reference = reference;
        this.amount = amount;
        this.product = product;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getGlCode() {
        return glCode;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public String getProduct() {
        return product;
    }
}

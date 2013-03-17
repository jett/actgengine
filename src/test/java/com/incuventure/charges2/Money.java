package com.incuventure.charges2;

import java.math.BigDecimal;

public class Money {

    String currency;
    BigDecimal amount;

    public Money(String currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

}

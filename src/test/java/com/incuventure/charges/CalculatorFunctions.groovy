package com.incuventure.charges

class CalculatorFunction {

    String currency;
    BigDecimal amount;

    BigDecimal result;


    public CalculatorFunction(String currency, BigDecimal amount) {
        this.currency = currency
        this.amount = amount
        this.result = amount
    }

    public CalculatorFunction add(BigDecimal add) {
        this.result = this.result.add(add)

        return this
    }

}

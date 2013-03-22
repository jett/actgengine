package com.incuventure.charges2

import org.junit.Test

class NonLcChargesTest {

    @Test
    public void testDPSettlement() {

        CurrencyConverter currencyConverter = new CurrencyConverter()

        currencyConverter.addRate("REG-SELL", "EUR", "USD", 1.339009)
        currencyConverter.addRate("REG-SELL", "USD", "PHP", 40.950000)
        currencyConverter.addRate("URR", "USD", "PHP", 40.750000)

        Map productDetails = [
                productCurrency : "EUR",
                productAmount : 50000.00,
                chargeSettlementCurrency: "PHP",
                settlement : [
                        [mode: "CASA", currency: "EUR", amount: "20000"],
                        [mode: "CASA", currency: "USD", amount: "40170.27"]
                ]
        ]

        ChargesCalculator calculator = new ChargesCalculator();

        calculator.setCurrencyConverter(currencyConverter)
        calculator.getNonLCCharges(productDetails)

    }

}

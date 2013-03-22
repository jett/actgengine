package com.incuventure.charges

import com.incuventure.charges2.CurrencyConverter
import org.junit.Before
import org.junit.Test

class TFSCalculatorTest {

    CurrencyConverter currencyConverter

    @Before
    public void setup() {
        currencyConverter = new CurrencyConverter()
    }

    @Test
    public void testCurrencyConverterConfig() {

        currencyConverter.addRate("SELL", "PHP", "USD", 40)
        currencyConverter.addRate("URR", "PHP", "USD", 50)

        println currencyConverter.getRate("SELL", "PHP", "USD")

        println "converted:: " + currencyConverter.convert("URR", "PHP", 55.55, "USD")

    }

}

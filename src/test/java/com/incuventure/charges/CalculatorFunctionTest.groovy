package com.incuventure.charges

import org.junit.Test


class CalculatorFunctionTest {

    @Test
    public void testFluent() {
        CalculatorFunction cf = new CalculatorFunction("USD", 50000).add(50);

        println cf.result
    }
}

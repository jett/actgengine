package com.incuventure.charges;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class CalculatorsTest {

    Calculators calculators;

    @Before
    public void setup() {
        calculators = new Calculators();
    }

    @Test
    public void testSucceedingFixed()  {

        BigDecimal result = calculators.firstSucceedingFixed(new BigDecimal(1499),
                new BigDecimal(500),
                new BigDecimal(10),
                new BigDecimal(500),
                new BigDecimal(1));

        Assert.assertEquals(result.compareTo(new BigDecimal("502")), 0);
    }

    @Test
    public void testFirstSucceedingPercentage() {

        BigDecimal result = calculators.firstSucceedingPercentage(new BigDecimal(1500),
                new BigDecimal(500),
                new BigDecimal(10),
                new BigDecimal(.25));

        Assert.assertEquals(result.compareTo(new BigDecimal("750")), 0);

    }

    @Test
    public void testForEvery()  {

        BigDecimal result = calculators.forEvery(new BigDecimal(999),
                new BigDecimal(200),
                new BigDecimal(1));

        Assert.assertEquals(result.compareTo(new BigDecimal("5")), 0);

    }

    @Test
    public void testDateTill() {

        BigDecimal months = calculators.getMonthsTill("3/13/2013", "4/13/2013");

        if(months.compareTo(BigDecimal.ONE) < 0) {
            months = BigDecimal.ONE;
        }

        System.out.println("months is: " + months);

        //calculators.get

    }

}

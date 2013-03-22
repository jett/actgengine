package com.incuventure.charges;

import org.jfree.date.SerialDate;
import org.jfree.date.SerialDateUtilities;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calculators {


    // generic routine to handle computation for
    // based on baseAmount, charge [firstCharge] for the first [firstAmount] and [forEveryCharge] for the succeeding
    // [forEveryAmount]

    public BigDecimal firstSucceedingFixed(BigDecimal baseAmount,
                               BigDecimal firstAmount,
                               BigDecimal firstCharge,
                               BigDecimal forEveryAmount,
                               BigDecimal forEveryCharge) {

        BigDecimal balance = baseAmount;
        BigDecimal totalCharge = BigDecimal.ZERO;

        if(balance.compareTo(BigDecimal.ZERO) > 0) {

            totalCharge = totalCharge.add(firstCharge);
            balance = balance.subtract(firstAmount);

        }

        while(balance.compareTo(BigDecimal.ZERO) > 0) {

            // todo: refactor to just divide
            balance = balance.subtract(forEveryAmount);
            totalCharge = totalCharge.add(forEveryCharge);
        }

        return totalCharge;

    }

    public BigDecimal firstSucceedingPercentage(BigDecimal baseAmount,
                                                           BigDecimal firstAmount,
                                                           BigDecimal firstCharge,
                                                           BigDecimal percentOfRemaining) {

        return firstSucceedingPercentageWithMinimum(baseAmount, firstAmount, firstCharge, percentOfRemaining, BigDecimal.ZERO);

    }

    public BigDecimal firstSucceedingPercentageWithMinimum(BigDecimal baseAmount,
                               BigDecimal firstAmount,
                               BigDecimal firstCharge,
                               BigDecimal percentOfRemaining, BigDecimal minimum) {

        BigDecimal balance = baseAmount;
        BigDecimal totalCharge = BigDecimal.ZERO;

        if(balance.compareTo(BigDecimal.ZERO) > 0) {

            // todo: refactor to just divide
            totalCharge = totalCharge.add(firstCharge);
            balance = balance.subtract(firstAmount);

            totalCharge = totalCharge.add(balance.multiply(percentOfRemaining));
        }

        if(minimum.compareTo(BigDecimal.ZERO) > 0 && totalCharge.compareTo(minimum) <= 0) {
            return minimum; //.setScale(2, BigDecimal.ROUND_UP);
        }

        return totalCharge; // .setScale(2, BigDecimal.ROUND_UP);
    }

    public BigDecimal forEvery(BigDecimal baseAmount,
                        BigDecimal everyAmount,
                        BigDecimal chargeForEvery) {

        BigDecimal balance = baseAmount;
        BigDecimal totalCharge = BigDecimal.ZERO;

        BigDecimal bg[] = baseAmount.divideAndRemainder(everyAmount);

        totalCharge = chargeForEvery.multiply(bg[0]);

        if(bg[1].compareTo(BigDecimal.ZERO) > 0) {
            totalCharge = totalCharge.add(chargeForEvery);
        }

        return totalCharge;

    }

    public BigDecimal percentageOf(BigDecimal amount, BigDecimal percentage ) {

        return amount.multiply(percentage); // .setScale(2, BigDecimal.ROUND_UP);

    }

    // date utilities
    public BigDecimal getMonthsTill(String dateFrom, String dateTo) {
//        System.out.println("getMonthsTill");
//        System.out.println("Date From:" + dateFrom);
//        System.out.println("Date To:" + dateTo);

        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");

        try {
            Date startDate = formatter.parse(dateFrom);
            Date endDate = formatter.parse(dateTo);
//            System.out.println("dateFrom:" + dateFrom);
//            System.out.println("dateTo:" + dateTo);

            //Uses Days 30/360 Convention
            Integer days = SerialDateUtilities.dayCount30ISDA(
                    SerialDate.createInstance(startDate),
                    SerialDate.createInstance(endDate)
            );

//            System.out.println("days 360:" + days);

            DateTime dateTimeFrom = new DateTime(startDate);
            DateTime dateTimeTo = new DateTime(endDate);
            int daysint = Days.daysBetween(dateTimeFrom, dateTimeTo).getDays();
            Integer daysInt = new Integer(daysint);

//            System.out.println("days 365:" + daysint);

            // get a rounded up version of this using a math hack
            // rounding up: (numerator + denominator-1) / denominator
            // rounding down: (numerator + (denominator)/2) / denominator
//            System.out.println("months:" + ((days + 29) / 30));
//            System.out.println("days:" + days);
//            System.out.println("months 360:" + new BigDecimal(days).divide(new BigDecimal("30"), 6, BigDecimal.ROUND_HALF_UP));
//            System.out.println("months 365:" + new BigDecimal(daysInt).divide(new BigDecimal("30"), 6, BigDecimal.ROUND_HALF_UP));
            return new BigDecimal(daysInt).divide(new BigDecimal("30"), 6, BigDecimal.ROUND_HALF_UP);

        } catch (Exception e) {
            // todo: handle invalid dates here
            return new BigDecimal(0);
        }
    }

}

package com.incuventure.charges2

import com.incuventure.charges.Calculators

class NonLCChargesCalculator extends ChargesCalculator{

    public Map compute(Map productDetails) {

        // precompute for the base variables
        precomputeBase(productDetails);

        Calculators calculators = new Calculators()

        // parameterized factors
        BigDecimal cilexFactor = 0.01.divide(4)
        BigDecimal bankCommissionFactor = 0.01.divide(8)
        BigDecimal cableFeeDefault = 1000
        BigDecimal bookingComissionDefault = 500
        BigDecimal notarialFeeDefault = 50
        BigDecimal bspRegFeeDefault = 100

        // charges
        BigDecimal bankComission = calculators.firstSucceedingPercentageWithMinimum((BigDecimal)getBaseVariable("chargesBasePHP"), 50000, 125, bankCommissionFactor, 1000)
        BigDecimal cableFee = cableFeeDefault
        BigDecimal cilex = calculators.percentageOf((BigDecimal)getBaseVariable("settledInForeignInUSD"), cilexFactor)
        BigDecimal bookingCommission = bookingComissionDefault;
        // TODO: add condition if TR loan is used
        BigDecimal docStamps = BigDecimal.ZERO


        println getBaseVariable("totalNotSettledByTRinPHP")
        println getBaseVariable("totalTrAmountInPHP")

        if(((BigDecimal)getBaseVariable("totalTrAmount")).compareTo(BigDecimal.ZERO) == 1) {
            docStamps = calculators.firstSucceedingFixed((BigDecimal)getBaseVariable("totalTrAmountInPHP"), 5000, 20, 5000, 10)
        }


        if ((BigDecimal)getBaseVariable("totalNotSettledByTRinPHP")?.compareTo(BigDecimal.ZERO)==1) {
            BigDecimal normalAmount = (BigDecimal)getBaseVariable("totalNotSettledByTRinPHP") - (BigDecimal)getBaseVariable("totalTrAmountInPHP")
            println "normalAmount:"+normalAmount
            docStamps = docStamps.add(calculators.forEvery( normalAmount, 200, 0.30))
        }

        BigDecimal notarialFee = notarialFeeDefault;
        BigDecimal bspRegFee = bspRegFeeDefault

        println "Bank Comission : " + bankComission.setScale(2, BigDecimal.ROUND_UP)
        println "Cable Fee : " + cableFee.setScale(2, BigDecimal.ROUND_UP)
        println "CILEX in PHP " + currencyConverter.convert("URR", "USD", cilex, "PHP").setScale(2, BigDecimal.ROUND_UP)
        println "Documentary Stamps: " + docStamps.setScale(2, BigDecimal.ROUND_UP)
        println "Booking Comission : " + bookingCommission.setScale(2, BigDecimal.ROUND_UP)
        println "Notarial Fee : " + notarialFee.setScale(2, BigDecimal.ROUND_UP)
        println "BSP Registration Fee : " + bspRegFee.setScale(2, BigDecimal.ROUND_UP)

        return [:]

    }


}

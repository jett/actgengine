package com.incuventure.charges2

import com.incuventure.charges.Calculators

class ChargesCalculator {

    CurrencyConverter currencyConverter

    public void setCurrencyConverter(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter
    }

    public Map getNonLCCharges(Map productDetails) {

        BigDecimal usdBase = BigDecimal.ZERO
        BigDecimal phpBase = BigDecimal.ZERO

        String productCurrency = productDetails.productCurrency
        BigDecimal productAmount = productDetails.productAmount
        String chargesSettlementCurrency = productDetails.chargesSettlementCurrency
        String productSettlementCurrency = ""

        // compute for the base
        if (!productCurrency.equalsIgnoreCase("USD") && !productCurrency.equalsIgnoreCase("PHP")) {

            // convert
            usdBase = currencyConverter.convert("REG-SELL", productCurrency, productAmount, "USD")
            phpBase = currencyConverter.convert("URR", "USD", usdBase, "PHP")

        } else if(productCurrency.equalsIgnoreCase("USD")) {

            usdBase = productAmount
            phpBase = currencyConverter.convert("URR", "USD", usdBase, "PHP")

        } else if (productCurrency.equalsIgnoreCase("PHP")) {

            phpBase = productAmount

        }

        // =========================================== product payment ============================================

        Map settlementPerCurrency = new HashMap<String, BigDecimal>()
        Map settlementPerModeCurrency = new HashMap<String, Object>()

        // total foreign currency settled (in USD)
        BigDecimal fcSettlementTotalInUsd = BigDecimal.ZERO

        // any third currency used to settle product payment
        String productSettlementThirdCurrency = ""

        // get the totals per currency per settlement mode
        if (productDetails.containsKey("settlement")) {

            productDetails.settlement.each() { settlement ->

                if (settlement.currency && settlement.amount && settlement.mode) {

                    BigDecimal amount = new BigDecimal(settlement.amount)
                    String currency = settlement.currency
                    String mode = settlement.mode

                    // total settlement per currency
                    if (!settlementPerCurrency.containsKey(currency)) {
                        settlementPerCurrency.put(currency, BigDecimal.ZERO)
                    }

                    BigDecimal newTotal = amount.add((BigDecimal)settlementPerCurrency.get(currency))
                    settlementPerCurrency.put(currency, newTotal)

                    // total settlement per currency per mode
                    if (!settlementPerModeCurrency[(mode)]) {
                        settlementPerModeCurrency.put(mode, [:])
                    }

                    println settlementPerModeCurrency

                    if (!settlementPerModeCurrency[(mode)][(currency)]) {
                        settlementPerModeCurrency[(mode)][(currency)] = BigDecimal.ZERO
                    }

                    newTotal = amount.add((BigDecimal)settlementPerModeCurrency[(mode)][(currency)])
                    settlementPerModeCurrency[(mode)][(currency)] = newTotal

                    // determine thirds currency
                    if (!settlement.currency.equalsIgnoreCase("USD") && !settlement.currency.equalsIgnoreCase("PHP")) {
                        productSettlementThirdCurrency = currency
                    }
                }
            }

            fcSettlementTotalInUsd = fcSettlementTotalInUsd.add(settlementPerCurrency.get("USD"));
            fcSettlementTotalInUsd = fcSettlementTotalInUsd.add(currencyConverter.convert("REG-SELL", productSettlementThirdCurrency, settlementPerCurrency.get(productSettlementThirdCurrency), "USD"));

        }

        println String.format("Product: %s %,.2f ", productCurrency, productAmount)
        println String.format("USD Base: USD %,.2f ", usdBase)
        println String.format("PHP Base: PHP %,.2f ", phpBase)

        println String.format("3C Settlement Total 3C %,.2f ", settlementPerCurrency.get(productSettlementThirdCurrency) ? BigDecimal.ZERO : settlementPerCurrency.get(productSettlementThirdCurrency))
        println String.format("USD Settlement Total USD %,.2f ", settlementPerCurrency.get("USD") ? settlementPerCurrency.get("USD") : BigDecimal.ZERO )
        println String.format("PHP Settlement Total PHP %,.2f ", settlementPerCurrency.get("PHP") ? settlementPerCurrency.get("PHP") : BigDecimal.ZERO )

        println String.format("Amount settled in foreign currency (in USD) %,.2f ", fcSettlementTotalInUsd)

        println "Settlement per mode: " + settlementPerModeCurrency


        // get payment NOT in TR loan
        BigDecimal totalNotSettledByTRinPHP = BigDecimal.ZERO

        BigDecimal totalNotInTrUSD = BigDecimal.ZERO
        BigDecimal totalNotInTrPHP = BigDecimal.ZERO

        settlementPerModeCurrency.each() { mode, settlements ->

            if(!((String)mode).equalsIgnoreCase("TR")) {

                settlements.each() { String currency, BigDecimal amount ->

                    if (currency.equalsIgnoreCase("USD")) {
                        println currency + " XXX " + amount
                        totalNotInTrUSD = totalNotInTrUSD.add(amount)
                    } else if (currency.equalsIgnoreCase("PHP")) {
                        totalNotInTrPHP = totalNotInTrPHP.add(amount)
                    } else {
                        println currency + " YYY " + amount
                        totalNotInTrUSD = totalNotInTrUSD.add(currencyConverter.convert("REG-SELL", currency, amount, "USD"))
                    }
                }

            }
        }

        totalNotSettledByTRinPHP = totalNotSettledByTRinPHP.add(totalNotInTrPHP)
        println "USD of non-tr " + totalNotInTrUSD
        totalNotSettledByTRinPHP = totalNotSettledByTRinPHP.add(currencyConverter.convert("URR", "USD", totalNotInTrUSD, "PHP"))

        println String.format("Total NOT settled via TR: %,.2f", totalNotSettledByTRinPHP)

        // =========================== calculation specific

        Calculators calculators = new Calculators()

        // parameterized factors
        BigDecimal cilexFactor = 0.01.divide(4)
        BigDecimal bankCommissionFactor = 0.01.divide(8)
        BigDecimal cableFeeDefault = 1000
        BigDecimal bookingComissionDefault = 500
        BigDecimal notarialFeeDefault = 50
        BigDecimal bspRegFeeDefault = 100

        println "cilex Factor: " + cilexFactor

        // charges
        BigDecimal bankComission = calculators.firstSucceedingPercentageWithMinimum(phpBase, 50000, 125, bankCommissionFactor, 1000)
        BigDecimal cableFee = cableFeeDefault
        BigDecimal cilex = calculators.percentageOf(fcSettlementTotalInUsd, cilexFactor)
        BigDecimal bookingCommission = bookingComissionDefault;
        BigDecimal notarialFee = notarialFeeDefault;
        BigDecimal bspRegFee = bspRegFeeDefault

        println "Bank Comission : " + bankComission.setScale(2, BigDecimal.ROUND_UP)
        println "Cable Fee : " + cableFee.setScale(2, BigDecimal.ROUND_UP)
        println "CILEX in USD " + cilex.setScale(2, BigDecimal.ROUND_UP)
        println "CILEX in PHP " + currencyConverter.convert("URR", "USD", cilex, "PHP").setScale(2, BigDecimal.ROUND_UP)
        println "Booking Comission : " + bookingCommission.setScale(2, BigDecimal.ROUND_UP)
        println "Notarial Fee : " + notarialFee.setScale(2, BigDecimal.ROUND_UP)
        println "BSP Registration Fee : " + bspRegFee.setScale(2, BigDecimal.ROUND_UP)

        return [:]

    }

}

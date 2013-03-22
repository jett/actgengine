package com.incuventure.charges2

class ChargesCalculator {

    CurrencyConverter currencyConverter
    Map <String, String> ratesConfig

    String thirdToUsdRateType
    String usdToPhpSingleRateType
    String usdToPhpMixedRateType
    String settlementToDraftUSDRateType

    Boolean mixedPayment

    Map results = new HashMap<String, Object>()


    public void setCurrencyConverter(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter
    }

    public void configRatesBasis(String thirdToUsd, String usdToPhpSingle, String usdToPhpMixed, String settlementToDraftUSD) {
        
            // rate type to use to convert thirds to USD
            this.thirdToUsdRateType = thirdToUsd

            // rate type to use to convert USD to PHP if a single currency is used for payment
            this.usdToPhpSingleRateType = usdToPhpSingle

            // rate type to use to convert USD to PHP if mixed currency is used for payment
            this.usdToPhpMixedRateType = usdToPhpMixed

            // rate type to use to convert settlement currency to the draft currency (currency of amount being paid)
            this.settlementToDraftUSDRateType = settlementToDraftUSD

    }

    protected Object getBaseVariable(String variableName) {

        return results.get(variableName)

    }

    protected void precomputeBase(Map productDetails) {

        BigDecimal usdBase = BigDecimal.ZERO
        BigDecimal phpBase = BigDecimal.ZERO

        String productCurrency = productDetails.productCurrency
        BigDecimal productAmount = productDetails.productAmount

        HashSet<String> productSettlementCurrencies = new HashSet<String>()

        String chargesSettlementCurrency = productDetails.chargesSettlementCurrency
        String productSettlementCurrency = ""

        Map settlementPerCurrency = new HashMap<String, BigDecimal>()
        Map settlementPerModeCurrency = new HashMap<String, Object>()

        // total foreign currency settled (in USD)
        BigDecimal fcSettlementTotalInUsd = BigDecimal.ZERO

        // any third currency used to settle product payment
        String productSettlementThirdCurrency = ""

        mixedPayment = false

        // get the totals per currency per settlement mode
        if (productDetails.containsKey("productSettlement")) {

            productDetails.productSettlement.each() { settlement ->

                if (settlement.currency && settlement.amount && settlement.mode) {

                    BigDecimal amount = new BigDecimal(settlement.amount)
                    String currency = settlement.currency
                    String mode = settlement.mode

                    productSettlementCurrencies.add(currency)

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

            if (productSettlementCurrencies.size() > 1 && productSettlementCurrencies.contains("PHP")) {
                mixedPayment = true
            }

            if (settlementPerCurrency.containsKey("USD")){
                fcSettlementTotalInUsd = fcSettlementTotalInUsd.add(settlementPerCurrency.get("USD"));
            }

            if (settlementPerCurrency.containsKey(productSettlementThirdCurrency)){
                fcSettlementTotalInUsd = fcSettlementTotalInUsd.add(currencyConverter.convert("REG-SELL", productSettlementThirdCurrency, settlementPerCurrency.get(productSettlementThirdCurrency), "USD"));
            }
        }

        // compute for the base
        if (!productCurrency.equalsIgnoreCase("USD") && !productCurrency.equalsIgnoreCase("PHP")) {

            // convert
            usdBase = currencyConverter.convert(thirdToUsdRateType, productCurrency, productAmount, "USD")
            phpBase = currencyConverter.convert(mixedPayment ? usdToPhpMixedRateType : usdToPhpSingleRateType, "USD", usdBase, "PHP")

        } else if(productCurrency.equalsIgnoreCase("USD")) {

            usdBase = productAmount
            phpBase = currencyConverter.convert(mixedPayment ? usdToPhpMixedRateType : usdToPhpSingleRateType, "USD", usdBase, "PHP")

        } else if (productCurrency.equalsIgnoreCase("PHP")) {

            phpBase = productAmount

        }

        // get payment NOT in TR loan
        BigDecimal totalNotSettledByTRinPHP = BigDecimal.ZERO

        BigDecimal totalNotInTrUSD = BigDecimal.ZERO
        BigDecimal totalNotInTrPHP = BigDecimal.ZERO

        String trCurrency
        BigDecimal totalTrAmount = BigDecimal.ZERO
        BigDecimal totalTrAmountInPHP = BigDecimal.ZERO

        settlementPerModeCurrency.each() { mode, settlements ->

            if(!((String)mode).equalsIgnoreCase("TR")) {

                settlements.each() { String currency, BigDecimal amount ->

                    if (currency.equalsIgnoreCase("USD")) {
                        totalNotInTrUSD = totalNotInTrUSD.add(amount)
                    } else if (currency.equalsIgnoreCase("PHP")) {
                        totalNotInTrPHP = totalNotInTrPHP.add(amount)
                    } else {
                        totalNotInTrUSD = totalNotInTrUSD.add(currencyConverter.convert(thirdToUsdRateType, currency, amount, "USD"))
                    }
                }

            } else {

                // settlement has TR

                settlements.each() { String currency, BigDecimal amount ->

                    // assumed that this
                    if (currency.equalsIgnoreCase("USD")) {
                        trCurrency = currency
                        totalTrAmount = totalTrAmount.add(amount)
                    } else if (currency.equalsIgnoreCase("PHP")) {
                        trCurrency = currency
                        totalTrAmount = totalTrAmount.add(amount)
                    }
                    // No TR for thirds

                }

            }
        }

        if (trCurrency.equalsIgnoreCase("USD")) {
            totalTrAmountInPHP = currencyConverter.convert(usdToPhpSingleRateType, trCurrency, totalTrAmount, "PHP")
        } else {
            totalTrAmountInPHP =  totalTrAmount
        }

        totalNotSettledByTRinPHP = totalNotSettledByTRinPHP.add(totalNotInTrPHP)
        totalNotSettledByTRinPHP = totalNotSettledByTRinPHP.add(currencyConverter.convert(mixedPayment ? usdToPhpMixedRateType : usdToPhpSingleRateType, "USD", totalNotInTrUSD, "PHP"))

        results.put("productCurrency", productCurrency)
        results.put("productAmount", productAmount)

        results.put("chargesBaseUSD", usdBase)
        results.put("chargesBasePHP", phpBase)

        results.put("productSettlementThirdTotals", settlementPerCurrency.get(productSettlementThirdCurrency) ? settlementPerCurrency.get(productSettlementThirdCurrency) : BigDecimal.ZERO )
        results.put("productSettlementUSDTotals", settlementPerCurrency.get("USD") ? settlementPerCurrency.get("USD") : BigDecimal.ZERO)
        results.put("productSettlementPHPTotals", settlementPerCurrency.get("PHP") ? settlementPerCurrency.get("PHP") : BigDecimal.ZERO)

        results.put("settledInForeignInUSD", fcSettlementTotalInUsd)

        results.put("totalNotSettledByTRinPHP", totalNotSettledByTRinPHP)

        results.put("trCurrency", trCurrency)
        results.put("totalTrAmount", totalTrAmount)
        results.put("totalTrAmountInPHP", totalTrAmountInPHP)


        results.each() { key, value ->

            println String.format("%s \t %s", key, value)

        }

        println "--------------------"

        // =========================== calculation specific



    }

}

package com.incuventure.charges2

class CurrencyConverter {

    Node rates;

    public CurrencyConverter() {
        rates = new Node(null, "rates");
    }

    public addRate(String rateType, String sourceCurrency, String targetCurrency, BigDecimal rate) {

        if(rates.get(sourceCurrency).isEmpty() ) {
            rates.appendNode(sourceCurrency, null)
        }

        Node baseNode = (Node) ((NodeList) rates.get(sourceCurrency)).get(0)

        NodeList targetNodeList = (NodeList) baseNode.get(targetCurrency)

        if (targetNodeList.isEmpty()) {
            baseNode.appendNode(targetCurrency, null)
        }

        Node targetNode = (Node) ((NodeList) baseNode.get(targetCurrency)).get(0)

        NodeList rateTypeList = (NodeList) targetNode.get(rateType)

        if (rateTypeList.isEmpty()) {
            targetNode.appendNode(rateType, null, rate)
        }

    }

    public getRate(String rateType, String sourceCurrency, String targetCurrency) {

        NodeList targetList = (NodeList) rates.get(sourceCurrency)

        if (targetList.isEmpty()) {
            return null
        }
        else {

            NodeList sourceList = (NodeList) targetList.get(0).get(targetCurrency)
            NodeList ratesList = (NodeList) sourceList.get(0).get(rateType)

            if (ratesList.isEmpty()) {
                return null
            }
            else {
                return ratesList.get(0).value()
            }

        }
    }

    public String getXml() {

        def sw = new StringWriter()
        new XmlNodePrinter(new PrintWriter(sw)).print(rates)
       return sw.toString()

    }

    public convert(String rateType, String sourceCurrency, BigDecimal baseAmount, String targetCurrency) {

        BigDecimal newValue = BigDecimal.ZERO;

        if (sourceCurrency.equalsIgnoreCase(targetCurrency)) {
            return baseAmount
        }

        BigDecimal rate = getRate(rateType, sourceCurrency, targetCurrency)

        if (rate == null) {

            // if no rate is available, try the reverse
            rate = getRate(rateType, targetCurrency, sourceCurrency)
            newValue = baseAmount.divide(rate,6,BigDecimal.ROUND_UP)

        } else {
            newValue = baseAmount.multiply(rate)
        }

        return newValue.setScale(2, BigDecimal.ROUND_UP)
    }

}

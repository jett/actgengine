package com.incuventure.charges2

class CurrencyConverter {

    Node rates;

    public CurrencyConverter() {
        rates = new Node(null, "rates");
    }

    public addRate(String rateType, String baseCurrency, String targetCurrency, BigDecimal rate) {

        if(rates.get(baseCurrency).isEmpty() ) {
            rates.appendNode(baseCurrency, null, baseCurrency)
        }

        Node baseNode = (Node) ((NodeList) rates.get(baseCurrency)).get(0)

        NodeList targetNodeList = (NodeList) baseNode.get(targetCurrency)

        if (targetNodeList.isEmpty()) {
            baseNode.appendNode(targetCurrency, null, targetCurrency)
        }

        Node targetNode = (Node) ((NodeList) baseNode.get(targetCurrency)).get(0)

        NodeList rateTypeList = (NodeList) targetNode.get(rateType)

        if (rateTypeList.isEmpty()) {
            targetNode.appendNode(rateType, null, rate)
        }

    }

    public getRate(String rateType, String baseCurrency, String targetCurrency) {

        NodeList baseList = (NodeList) rates.get(baseCurrency)

        if (baseList.isEmpty()) {
            return null
        }
        else {

            NodeList targetList = (NodeList) baseList.get(0).get(targetCurrency)

            NodeList ratesList = (NodeList) targetList.get(0).get(rateType)

            if (ratesList.isEmpty()) {
                return null
            }
            else {
                return ratesList.get(0).value()
            }

        }
    }

    public convert(String rateType, String baseCurrency, BigDecimal baseAmount, String targetCurrency) {

        BigDecimal rate = getRate(rateType, baseCurrency, targetCurrency)

        BigDecimal newValue = new BigDecimal(baseAmount).divide(rate)

        return newValue
    }

}

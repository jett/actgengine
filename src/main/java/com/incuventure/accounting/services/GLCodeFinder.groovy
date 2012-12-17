package com.incuventure.accounting.services

import com.incuventure.accounting.GLReference

class GLCodeFinder {

    static def chargeGLReference = [
            "CORRES" : [
                    "*" : [
                            "909" : [ "RG" : ["PHP" : new GLReference("909", "RG", "PHP", "268610105000", "A/P CORRES- ADVISING FEE", null, null)]]
                    ]
            ],
            "CONFIRMING" : [
                    "*" : [
                            "909" : [ "RG" : ["PHP" : new GLReference("909", "RG", "PHP", "268610105000", "A/P CORRES- CONFIRMING FEE", null, null)]]
                    ]
            ],
            "CABLE" : [
                    "*" : [
                            "909" : [ "RG" : ["PHP" : new GLReference("909", "RG", "PHP", "268610106000", "ACCTS PAYABLE-RES-CABLE", null, null)]]
                    ]
            ],
            "DOCSTAMP" : [
                    "*" : [
                            "909" : [ "RG" : ["PHP" : new GLReference("909", "RG", "PHP", "268610101000", "ACCTS PAYABLE-RES-EDST", null, null)]]
                    ]
            ],
            "SUPPLIES" : [
                    "*" : [
                            "909" : [ "RG" : ["PHP" : new GLReference("*", "RG", "PHP", "661518010000", "SS USED-OFFICE SUPPLIES", null, null)]]
                    ]
            ],
            "ADVISING" : [
                    "*" : [
                            "909" : [ "RG" : ["PHP" : new GLReference("*", "RG", "PHP", "268610105000", "A/P CORRES- ADVISING FEE", null, null)]]
                    ]
            ],
            "CONFIRM" : [
                    "*" : [
                            "909" : [ "RG" : ["PHP" : new GLReference("*", "RG", "PHP", "268610105000", "A/P CORRES- CONFIRMING FEE", null, null)]]
                    ]
            ],
            "BANKCOM" : [
                    "SIGHTLC" : [
                            "909" : [ "RG" : ["PHP" : new GLReference("*", "RG", "PHP", "561201680000", "BANK COM-OPENING OF SIGHT IMPORT LC", null, null)]]
                    ]
            ]
    ]


    static def contingentReference = [
            "CASH-LC" : [
                    "USD" : [
                            "909" : [ "RG" : new GLReference("909", "RG", "USD", "825110102000", "CCL-COMMERCIAL LC-SIGHT LC-FOREIGN", "835110102000", "COMMERCIAL LC-SIGHT LC-FOREIGN")]]
            ]
    ]

    static def paymentReference = [
            "CASA" : new GLReference("*", "*", "*", "xxxxxxxxxxxx", "CASA", null, null),           // this will be suspense
            "APPLY-AP" : new GLReference("*", "*", "*", "268610189000", "AP-RES-OTHERS", null, null),
            "AP-REMITTANCE" : new GLReference("*", "*", "*", "268610184500", "AP-REMITTANCE", null, null),
            "AR" : new GLReference("*", "*", "*", "179130102100", "AR", null, null)
    ]



    public static GLReference getChargeGLReference(String charge, String product, String unitCode, String bookCode, String currency) {

        GLReference match = chargeGLReference[charge][product][unitCode][bookCode][currency]

        return match

    }

    public static GLReference getProductBasedChargeGLReference(String charge, String product, String unitCode, String bookCode, String currency) {

        GLReference match = chargeGLReference[charge][product][unitCode][bookCode][currency]

        return match

    }

    public static GLReference getContingentGLReference(String productCode, String currency, String unitCode) {

        GLReference match = contingentReference[productCode][currency][unitCode]["RG"]

        return match

    }

    public static GLReference getPaymentGLReference(String paymentMode, String bookCode, String unitcode, String currency) {

        GLReference match = paymentReference[paymentMode]

        // override asterisked values
        if (match != null) {

            if (match.getUnitCode().equalsIgnoreCase("*")) {
                match.setUnitCode(unitcode);
            }

            if (match.getCurrency().equalsIgnoreCase("*")) {
                match.setCurrency(currency);
            }

            if (match.getCurrency().equalsIgnoreCase("*")) {
                match.setCurrency(currency)
            }

            // set the book code based on the currency
            if (match.getCurrency().equalsIgnoreCase("PHP")) {
                match.setBookCode("RG")
            }
            else {
                match.setBookCode("FC")
            }
        }

        return match

    }

}

package com.incuventure.accounting;

public class GLReference {

    public String code;
    public String currency;
    public String description;
    public String contraCode;

    public GLReference(String code, String contraCode, String currency, String description ) {
        this.code = code;
        this.currency = currency;
        this.description = description;
        this.contraCode = contraCode;
    }

    public String getCode() {

        return code;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getContraCode() {
        return contraCode;
    }
}

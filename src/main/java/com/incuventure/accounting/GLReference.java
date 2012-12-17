package com.incuventure.accounting;

public class GLReference {

    public String unitCode;
    public String bookCode;

    public String getBookCode() {
        return bookCode;
    }

    public String currency;
    public String description;
    public String code;
    public String contraCode;
    public String contraDescription;

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getContraDescription() {
        return contraDescription;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        if(this.unitCode.equalsIgnoreCase("*")) {
            this.unitCode = unitCode;
        }
    }

    public void setBookCode(String bookCode) {
        if(this.bookCode.equalsIgnoreCase("*")) {
            this.bookCode = bookCode;
        }
    }

    public void setCode(String code) {
        if(this.code.equalsIgnoreCase("*")) {
            this.code = code;
        }
    }

    public GLReference(String unitCode, String bookCode, String currency, String code, String description, String contraCode, String contraDescription) {
        this.unitCode = unitCode;
        this.currency = currency;
        this.description = description;
        this.code = code;
        this.contraCode = contraCode;
        this.bookCode = bookCode;
        this.contraDescription = contraDescription;
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

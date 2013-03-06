package com.incuventure.accounting.domain;

import com.incuventure.accounting.AccountingEntry;
import com.incuventure.accounting.GLReference;
import com.incuventure.accounting.services.GLCodeFinder;

import java.math.BigDecimal;
import java.util.List;

public class TFSAccountingLedger {

    Ledger ledger;

    public TFSAccountingLedger() {
        ledger = new Ledger();
    }

    public void addChargeEntry(String reference, String chargeCode, String product, BigDecimal amount, String unitCode) {

        GLReference glref = GLCodeFinder.getChargeGLReference(chargeCode, product, unitCode, "RG", "PHP");

        if(glref != null) {

            // TODO: add description
            ledger.addEntry(AccountingEntryType.CHARGE,
                    reference,  "C",
                    glref.getUnitCode().equalsIgnoreCase("*") ? unitCode : glref.getUnitCode(),
                    glref.getBookCode(),
                    glref.getCode(),
                    glref.getDescription(),
                    glref.getCurrency(),
                    amount,
                    "");

        } else {
            System.out.println("error here");
        }

    }

    public void addPaymentEntry(String reference, String paymentMode, String currency, BigDecimal amount, String unitCode) {

        GLReference glref = GLCodeFinder.getPaymentGLReference(paymentMode, "*", unitCode, currency);
        if(glref != null) {

            // TODO: add description
            ledger.addEntry(AccountingEntryType.PAYMENT,
                    reference, "D",
                    glref.getUnitCode(),
                    glref.getBookCode(),
                    glref.getCode(),
                    glref.getDescription(),
                    glref.getCurrency(), amount, "");
        } else {
            System.out.println("error here");
        }

    }

    public List<AccountingEntry> getEntries() {
        return ledger.getEntries();
    }

    // mark the payments as final so we can put in entries for excess payment
    public void finalizePayment(Boolean withCWT, BigDecimal urrPesoToUSD) {

        String reference = "";

        BigDecimal paymentTotalInPHP = new BigDecimal(0);
        BigDecimal paymentTotalInUSD = new BigDecimal(0);
        BigDecimal chargesTotalInPHP = new BigDecimal(0);

        // get the total peso amount of the payments
        for(AccountingEntry entry : ledger.getEntriesOfType(AccountingEntryType.PAYMENT)) {

            BigDecimal baseAmount = new BigDecimal(0);

            if(entry.getCurrency().equalsIgnoreCase("PHP")) {
                // if PHP, add as-is
                baseAmount = entry.getAmount();
                paymentTotalInPHP = paymentTotalInPHP.add(baseAmount);
            } else if(entry.getCurrency().equalsIgnoreCase("USD")) {
                // if USD, convert to PHP
                baseAmount = entry.getAmount();
                paymentTotalInUSD = paymentTotalInUSD.add(baseAmount);
            }
        }


        // convert all $ to PHP to get the PHP total
        paymentTotalInPHP = paymentTotalInPHP.add(paymentTotalInUSD.multiply(urrPesoToUSD));

        // get the total peso amount of the payments
        for(AccountingEntry entry : ledger.getEntriesOfType(AccountingEntryType.CHARGE)) {

            reference = entry.getReference();
            BigDecimal baseAmount = new BigDecimal(0);

            if(entry.getCurrency().equalsIgnoreCase("PHP")) {
                // if PHP, add as-is
                baseAmount = entry.getAmount();
            } else if(entry.getCurrency().equalsIgnoreCase("USD")) {
                // if USD, convert to PHP
                baseAmount = entry.getAmount().multiply(urrPesoToUSD);
            }

            chargesTotalInPHP = chargesTotalInPHP.add(baseAmount);
        }


        // round before we compare
        chargesTotalInPHP = chargesTotalInPHP.setScale(2, BigDecimal.ROUND_HALF_UP);
        paymentTotalInPHP = paymentTotalInPHP.setScale(2, BigDecimal.ROUND_HALF_UP);
        paymentTotalInUSD = paymentTotalInUSD.setScale(2, BigDecimal.ROUND_HALF_UP);

        System.out.println("Total Payment USD: " + paymentTotalInUSD);
        System.out.println("Total Payment Peso: " + paymentTotalInPHP);
        System.out.println("Total Charges Peso: " + chargesTotalInPHP);

        // if payment is greater than charges, book excess payment
        if(paymentTotalInPHP.compareTo(chargesTotalInPHP) > 0) {

            BigDecimal APAmount = paymentTotalInPHP.subtract(chargesTotalInPHP);
            System.out.println("Overpayment: " + APAmount);

            ledger.addEntry(AccountingEntryType.PAYMENT,
                    reference,
                    "C",
                    "909",
                    "RG",
                    "268610189000",
                    "AP-RES-OTHERS",
                    "PHP", APAmount, "");
        }

        // if payment was made in USD, we need to put in the entries to make it cross books
        // we need 2 pairs of entries, one to cross from FC to RG and another to convert the RG-USD
        // to RG-PHP
        if(paymentTotalInUSD.compareTo(new BigDecimal(0)) > 0) {

            ledger.addEntry(AccountingEntryType.XFER,
                    reference,
                    "C",
                    "900",
                    "FC",
                    "185110101100",
                    "DUE FROM/TO RBU",
                    "USD", paymentTotalInUSD, "");

            ledger.addEntry(AccountingEntryType.XFER,
                    reference,
                    "D",
                    "900",
                    "RG",
                    "185110101200",
                    "DUE FROM/TO FCDU",
                    "USD", paymentTotalInUSD, "");

            ledger.addEntry(AccountingEntryType.XFER,
                    reference,
                    "C",
                    "909",
                    "RG",
                    "280111010000",
                    "SPOT TRADE",
                    "USD", paymentTotalInUSD, "");

            ledger.addEntry(AccountingEntryType.XFER,
                    reference,
                    "D",
                    "909",
                    "RG",
                    "280111010000",
                    "SPOT TRADE",
                    "PHP", paymentTotalInPHP, "");
        }

        if(withCWT) {

            // if CWT is included, determine the amount for CWT
            // TODO: the charge code for commissions on which CILEX is based on is currently hardcoded
            List<AccountingEntry> commissions = ledger.getEntriesOfTypeAndCode(AccountingEntryType.CHARGE, "561201680000");

            //todo: add cilex in thirds
            BigDecimal cwtInUSD = new BigDecimal(0);
            BigDecimal baseForCWTinPHP = new BigDecimal(0);

            for(AccountingEntry entry : commissions) {
                baseForCWTinPHP = baseForCWTinPHP.add(entry.getAmount());
            }

            // get the base for CWT
            baseForCWTinPHP = baseForCWTinPHP.multiply(new BigDecimal(0.02)).setScale(2, BigDecimal.ROUND_HALF_UP);

            System.out.println("base for CILEX: " + baseForCWTinPHP);


            // TODO : where do we put CWT when there are multiple currency payments
            // if payment was made in PHP, we add entries for peso based
            if(paymentTotalInPHP.compareTo(new BigDecimal(0)) > 0) {
                ledger.addEntry(AccountingEntryType.CWT,
                    reference,
                    "D",
                    "909",
                    "RG",
                    "179230112300",
                    "MISC. ASSET - 2% CWT",
                    "PHP", baseForCWTinPHP, "");
            }



            //cilexInUSD = baseForCILEX.divide(urrPesoToUSD);
//            cwtInUSD = baseForCWTinPHP.divide(new BigDecimal(42.45), RoundingMode.HALF_UP);
//            cwtInUSD = cwtInUSD.setScale(2, BigDecimal.ROUND_HALF_UP);
//
//            System.out.println("CILEX in USD: " + cwtInUSD);
//
//            ledger.addEntry(AccountingEntryType.CWT,
//                    reference,
//                    "C",
//                    "909",
//                    "FC",
//                    "179230402009",
//                    "TRADE SUSPENSE (for 2% CWT)",
//                    "USD", cwtInUSD, "");
//
//            ledger.addEntry(AccountingEntryType.XFER,
//                    reference,
//                    "D",
//                    "900",
//                    "FC",
//                    "185110101100",
//                    "DUE FROM/TO FCDU",
//                    "USD", paymentTotalInUSD, "");

        }

    }

}

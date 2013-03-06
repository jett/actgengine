package com.incuventure.accounting;


import com.incuventure.accounting.domain.TFSAccountingLedger;
import org.junit.Test;

import java.math.BigDecimal;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:/test-context.xml"})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AccountingTest {

//    @Autowired
//    DomainEventPublisher eventPublisher;

//    @Autowired
//    Ledger ledger;


    @Test
    public void testChargesPesoSettlementOverpayment() {

        TFSAccountingLedger accountingLedger = new TFSAccountingLedger();

        accountingLedger.addChargeEntry("test", "CABLE", "*", new BigDecimal("800"), "909");
        accountingLedger.addChargeEntry("test", "DOCSTAMP", "*", new BigDecimal("1278"), "909");
        accountingLedger.addChargeEntry("test", "BANKCOM", "SIGHTLC", new BigDecimal("10579"), "909");
        accountingLedger.addChargeEntry("test", "SUPPLIES", "*", new BigDecimal("50"), "909");
        accountingLedger.addChargeEntry("test", "ADVISING", "*", new BigDecimal("2130"), "909");
        accountingLedger.addChargeEntry("test", "CONFIRM", "*", new BigDecimal("5289.50"), "909");

        accountingLedger.addPaymentEntry("test", "CASA", "PHP", new BigDecimal("20992.50"), "909");
        accountingLedger.addPaymentEntry("test", "CHECK", "PHP", new BigDecimal("2000"), "909");
        accountingLedger.addPaymentEntry("test", "APPLY-AP", "PHP", new BigDecimal("1555"), "909");

        accountingLedger.finalizePayment(false, new BigDecimal(40));

        BigDecimal totalCreditPHP = new BigDecimal(0);
        BigDecimal totalDebitPHP = new BigDecimal(0);

        for(AccountingEntry entry : accountingLedger.getEntries()) {

            if(entry.getAction().equalsIgnoreCase("C")) {
                totalCreditPHP = totalCreditPHP.add(entry.getAmount());
            }

            if(entry.getAction().equalsIgnoreCase("D")) {
                totalDebitPHP = totalDebitPHP.add(entry.getAmount());
            }

            System.out.println(entry);
        }

        System.out.println("Total PHP Credit: " + totalCreditPHP.toString());
        System.out.println("Total PHP Debit: " + totalDebitPHP.toString());


    }

    @Test
    public void testChargesUSDSettlement() {

        TFSAccountingLedger accountingLedger = new TFSAccountingLedger();

        accountingLedger.addChargeEntry("test", "CABLE", "*", new BigDecimal("800"), "909");
        accountingLedger.addChargeEntry("test", "DOCSTAMP", "*", new BigDecimal("1273.50"), "909");
        accountingLedger.addChargeEntry("test", "BANKCOM", "SIGHTLC", new BigDecimal("5270.88"), "909");
        accountingLedger.addChargeEntry("test", "SUPPLIES", "*", new BigDecimal("50"), "909");
        accountingLedger.addChargeEntry("test", "CILEX", "*", new BigDecimal("2122.50"), "909");
        accountingLedger.addChargeEntry("test", "ADVISING", "*", new BigDecimal("2122.50"), "909");
        accountingLedger.addChargeEntry("test", "CONFIRM", "*", new BigDecimal("5270.88"), "909");

        accountingLedger.addPaymentEntry("test", "CASA", "USD", new BigDecimal("100"), "909");
        accountingLedger.addPaymentEntry("test", "APPLY-AP", "USD", new BigDecimal("50"), "909");
        accountingLedger.addPaymentEntry("test", "AP-REMITTANCE", "USD", new BigDecimal("50"), "909");
        accountingLedger.addPaymentEntry("test", "AR", "USD", new BigDecimal("198.36"), "909");

        accountingLedger.finalizePayment(false, new BigDecimal(42.45));

        BigDecimal totalCreditPHP = new BigDecimal(0);
        BigDecimal totalDebitPHP = new BigDecimal(0);

        for(AccountingEntry entry : accountingLedger.getEntries()) {

            if(entry.getAction().equalsIgnoreCase("C")) {
                totalCreditPHP = totalCreditPHP.add(entry.getAmount());
            }

            if(entry.getAction().equalsIgnoreCase("D")) {
                totalDebitPHP = totalDebitPHP.add(entry.getAmount());
            }

            System.out.println(entry);
        }

        System.out.println("Total PHP Credit: " + totalCreditPHP.toString());
        System.out.println("Total PHP Debit: " + totalDebitPHP.toString());

    }

    @Test
    public void testChargesPHPSettlementCWT() {

        TFSAccountingLedger accountingLedger = new TFSAccountingLedger();

        accountingLedger.addChargeEntry("test", "CABLE", "*", new BigDecimal("800"), "909");
        accountingLedger.addChargeEntry("test", "DOCSTAMP", "*", new BigDecimal("1278"), "909");
        accountingLedger.addChargeEntry("test", "BANKCOM", "SIGHTLC", new BigDecimal("5289.50"), "909");
        accountingLedger.addChargeEntry("test", "SUPPLIES", "*", new BigDecimal("50"), "909");
        accountingLedger.addChargeEntry("test", "ADVISING", "*", new BigDecimal("2130"), "909");
        accountingLedger.addChargeEntry("test", "CONFIRM", "*", new BigDecimal("5289.50"), "909");

        accountingLedger.addPaymentEntry("test", "CASA", "PHP", new BigDecimal("4837"), "909");
        accountingLedger.addPaymentEntry("test", "CHECK", "PHP", new BigDecimal("2000"), "909");
        accountingLedger.addPaymentEntry("test", "IBT", "PHP", new BigDecimal("2500"), "909");
        accountingLedger.addPaymentEntry("test", "APPLY-AP", "PHP", new BigDecimal("1500"), "909");
        accountingLedger.addPaymentEntry("test", "AP-REMITTANCE", "PHP", new BigDecimal("3000"), "909");
        accountingLedger.addPaymentEntry("test", "AR", "PHP", new BigDecimal("894.31"), "909"); // todo: CDT Was deducted here

        accountingLedger.finalizePayment(true, new BigDecimal(42.45));

        BigDecimal totalCreditPHP = new BigDecimal(0);
        BigDecimal totalDebitPHP = new BigDecimal(0);

        for(AccountingEntry entry : accountingLedger.getEntries()) {

            if(entry.getAction().equalsIgnoreCase("C")) {
                totalCreditPHP = totalCreditPHP.add(entry.getAmount());
            }

            if(entry.getAction().equalsIgnoreCase("D")) {
                totalDebitPHP = totalDebitPHP.add(entry.getAmount());
            }

            System.out.println(entry);
        }

        System.out.println("Total PHP Credit: " + totalCreditPHP.toString());
        System.out.println("Total PHP Debit: " + totalDebitPHP.toString());

    }

}

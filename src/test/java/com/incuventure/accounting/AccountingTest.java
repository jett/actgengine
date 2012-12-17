package com.incuventure.accounting;


import com.incuventure.accounting.event.charge.ClientChargedEvent;
import com.incuventure.accounting.event.payment.ClientPaidEvent;
import com.incuventure.accounting.event.product.ContingentLiabilityIncurredEvent;
import com.incuventure.accounting.services.GLCodeFinder;
import com.incuventure.accounting.services.Ledger;
import com.incuventure.ddd.domain.DomainEventPublisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

public class AccountingTest {

    @Autowired
    DomainEventPublisher eventPublisher;

    @Autowired
    Ledger ledger;

    @Test
    public void testEvent() {

        Map chargeDetails = new HashMap();

        eventPublisher.publish(new ClientChargedEvent("test", "CORRES", "*", new BigInteger("20"), "909"));
        eventPublisher.publish(new ClientChargedEvent("test", "CABLE", "*", new BigInteger("120"), "909"));

        eventPublisher.publish(new ContingentLiabilityIncurredEvent("test", "CASH-LC", "USD", new BigInteger("1000000"), "LC122-121-12"));

        eventPublisher.publish(new ClientPaidEvent("test", "CASA", "PHP", new BigInteger("50000"), "909"));

        // product specific events

        // clientcharged events
        // charges payment detail paid (for each mode)
        // product payment detail paid (for each mode)
        // all charges paid
        // all product charges paid

        for(AccountingEntry entry : ledger.getEntries()) {
            System.out.println(entry);
        }

    }

}

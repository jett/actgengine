package com.incuventure.accounting;


import com.incuventure.accounting.event.CashPurchaseEvent;
import com.incuventure.ddd.domain.DomainEventPublisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

public class AccountingTest {

    @Autowired
    DomainEventPublisher eventPublisher;

    @Test
    public void testEvent() {
        eventPublisher.publish(new CashPurchaseEvent("sample", 100));
    }


}

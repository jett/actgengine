package com.incuventure.accounting.handler;

import com.incuventure.accounting.event.CashPurchaseEvent;
import com.incuventure.ddd.infrastructure.events.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PurchaseEventHandlers {


    @EventListener
    public void handleCashPurchase(CashPurchaseEvent event){

        System.out.println("I handled the event with amount: " + event.getAmount());

    }
}

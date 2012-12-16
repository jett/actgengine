package com.incuventure.accounting.event;

import com.incuventure.ddd.domain.DomainEvent;

public class CashPurchaseEvent extends PurchaseEvent implements DomainEvent {

        int amount;

        public CashPurchaseEvent(String item, int amount){
            super(item);
            this.amount = amount;
        }

    public int getAmount() {
        return amount;
    }
}

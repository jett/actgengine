package com.incuventure.accounting.event;

public abstract class PurchaseEvent {

    String param;

    public PurchaseEvent(String param) {
        this.param = param;
    }
}

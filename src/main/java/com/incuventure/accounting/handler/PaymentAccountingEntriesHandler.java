package com.incuventure.accounting.handler;


import com.incuventure.accounting.GLReference;
import com.incuventure.accounting.event.charge.ClientChargedEvent;
import com.incuventure.accounting.event.payment.ClientPaidEvent;
import com.incuventure.accounting.services.GLCodeFinder;
import com.incuventure.accounting.services.Ledger;
import com.incuventure.ddd.infrastructure.events.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentAccountingEntriesHandler {

    @Autowired
    Ledger ledger;

    @EventListener
    public void bookAccountingEntriesForPayment(ClientPaidEvent clientPaidEvent) {

        GLReference glref = GLCodeFinder.getPaymentGLReference(clientPaidEvent.getPaymentMode(), "*", clientPaidEvent.getUnitCode(), clientPaidEvent.getCurrency());
        if(glref != null) {
//            System.out.println("ref:" + clientPaidEvent.getReference() + "\t CR: " + glref.getUnitCode() +"\t" + glref.getBookCode() +  "\t" + glref.getCode() + "\t" + glref.getDescription() + "\t" + glref.getCurrency() + "\t" + clientPaidEvent.getAmount());

            ledger.addEntry(clientPaidEvent.getReference(), "C", glref.getUnitCode(), glref.getBookCode(), glref.getCode(), glref.getDescription(), glref.getCurrency(), clientPaidEvent.getAmount(), clientPaidEvent.getDescription());
        } else {
            System.out.println("error here");
        }

    }
}

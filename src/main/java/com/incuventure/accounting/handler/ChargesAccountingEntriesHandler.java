package com.incuventure.accounting.handler;


import com.incuventure.accounting.GLReference;
import com.incuventure.accounting.event.charge.ClientChargedEvent;
import com.incuventure.accounting.services.GLCodeFinder;
import com.incuventure.accounting.services.Ledger;
import com.incuventure.ddd.infrastructure.events.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChargesAccountingEntriesHandler {

    @Autowired
    Ledger ledger;

    @EventListener
    public void bookAccountingEntriesForCharge(ClientChargedEvent clientChargedEvent) {

        GLReference glref = GLCodeFinder.getChargeGLReference(clientChargedEvent.getChargeCode(), clientChargedEvent.getProduct(), clientChargedEvent.getUnitCode(), "RG", "PHP");
        if(glref != null) {
//            System.out.println("ref:" + clientChargedEvent.getReference() + "\t CR: " + glref.getUnitCode() +"\t" + glref.getBookCode() +  "\t" + glref.getCode() + "\t" + glref.getDescription() + "\t" + glref.getCurrency() + "\t" + clientChargedEvent.getAmount());

            ledger.addEntry(clientChargedEvent.getReference(),  "C", glref.getUnitCode(), glref.getBookCode(), glref.getCode(), glref.getDescription(), glref.getCurrency(), clientChargedEvent.getAmount(), clientChargedEvent.getDescription());

        } else {
            System.out.println("error here");
        }

    }

}

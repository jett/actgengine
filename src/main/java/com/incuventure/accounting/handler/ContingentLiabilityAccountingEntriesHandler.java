package com.incuventure.accounting.handler;

import com.incuventure.accounting.GLReference;
import com.incuventure.accounting.event.product.ContingentLiabilityIncurredEvent;
import com.incuventure.accounting.services.GLCodeFinder;
import com.incuventure.accounting.services.Ledger;
import com.incuventure.ddd.infrastructure.events.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContingentLiabilityAccountingEntriesHandler {

    @Autowired
    Ledger ledger;

    @EventListener
    public void bookAccountingEntriesForCL(ContingentLiabilityIncurredEvent clIncurredEvent) {

        GLReference glref = GLCodeFinder.getContingentGLReference(clIncurredEvent.getProductCode(), clIncurredEvent.getCurrency(), "909");
        if(glref != null) {
//            System.out.println("ref:" + clIncurredEvent.getReference() + "\t CR: " + glref.getUnitCode() +"\t" + glref.getBookCode() +"\t" + glref.getCode() + "\t" + glref.getDescription() + "\t" + glref.getCurrency() + "\t" + clIncurredEvent.getAmount());
//            System.out.println("ref:" + clIncurredEvent.getReference() + "\t DR: " + glref.getUnitCode() +"\t" + glref.getBookCode() +"\t" + glref.getContraCode()+ "\t" + glref.getContraDescription() + "\t" + glref.getCurrency() + "\t" + clIncurredEvent.getAmount());

            ledger.addEntry(clIncurredEvent.getReference(), "C", glref.getUnitCode(), glref.getBookCode(), glref.getCode(), glref.getDescription(), glref.getCurrency(), clIncurredEvent.getAmount(), clIncurredEvent.getDescription());
            ledger.addEntry(clIncurredEvent.getReference(), "D", glref.getContraCode(), glref.getBookCode(), glref.getContraCode(), glref.getContraDescription(), glref.getCurrency(), clIncurredEvent.getAmount(), clIncurredEvent.getDescription());

        } else {
            System.out.println("error here");
        }

    }


}

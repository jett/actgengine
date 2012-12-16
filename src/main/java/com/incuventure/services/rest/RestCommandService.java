package com.incuventure.services.rest;

import com.google.gson.Gson;
import com.incuventure.accounting.event.CashPurchaseEvent;
import com.incuventure.accounting.event.TestInterface;
import com.incuventure.ddd.domain.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

@Path("/fire")
@Component
public class RestCommandService {

    @Autowired
    DomainEventPublisher eventPublisher;

    @Autowired
    TestInterface messenger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/{domainObject}")
    public Response executeCommand(@Context UriInfo allUri) {

        Gson gson = new Gson();

        String result="";
        Map returnMap = new HashMap();

//        System.out.println("body in map: " + jsonParams);


        if(messenger == null ) {
            System.out.println("messenger is null");
        }
        else {
            messenger.say("you found me!");
        }

        if(eventPublisher == null) {
            System.out.println("eventpublisher is null!");
        }

        eventPublisher.publish(new CashPurchaseEvent("sample", 100));

        returnMap.put("status", "ok");
        returnMap.put("response", "xxxxx");

        // format return data as json
        result = gson.toJson(returnMap);

        // todo: we should probably return the appropriate HTTP error codes instead of always returning 200
        return Response.status(200).entity(result).build();
    }
}

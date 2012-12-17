package com.incuventure.services.rest;

import com.google.gson.Gson;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/{domainObject}")
    public Response executeCommand(@Context UriInfo allUri) {

        Gson gson = new Gson();

        String result="";
        Map returnMap = new HashMap();

        if(eventPublisher == null) {
            System.out.println("eventpublisher is null!");
        }

        returnMap.put("status", "ok");
        returnMap.put("response", "xxxxx");

        // format return data as json
        result = gson.toJson(returnMap);

        // todo: we should probably return the appropriate HTTP error codes instead of always returning 200
        return Response.status(200).entity(result).build();
    }
}

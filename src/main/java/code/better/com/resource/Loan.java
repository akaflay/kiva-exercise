package code.better.com.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import code.better.com.service.ILoan;

@Component
@Path("/loans")
public class HelloWorld {

    @Autowired
    private ILoan service;

    @GET
    public void get(@Suspended AsyncResponse response) {

        service.get(response, null);
    }

    @GET
    @Path("/{id}")
    public void getById(@Suspended AsyncResponse response,
                               @PathParam("id") String loanId) {

        service.get(response, loanId);
    }
    @GET
    @Path("/{id}/lenders")
    public void secondEndPoint(@Suspended AsyncResponse response,
                               @PathParam("id") String loanId) {

        service.getLenders(response, loanId);
    }

}

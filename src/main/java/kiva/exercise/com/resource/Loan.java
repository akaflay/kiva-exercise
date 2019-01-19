package kiva.exercise.com.resource;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import kiva.exercise.com.pojo.LenderPayment;
import kiva.exercise.com.service.ILoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Path("/loans")
public class Loan {

    @Autowired
    private ILoan service;

    @GET
    public void get(@Suspended final AsyncResponse response) {

        service.get(response, null);
    }

    @GET
    @Path("/{id}")
    public void getById(@Suspended final AsyncResponse response,
                        @PathParam("id") final String loanId) {

        service.get(response, loanId);
    }
    @GET
    @Path("/{id}/lenders")
    public void getLenders(@Suspended final AsyncResponse response,
                               @PathParam("id") final String loanId) {

        service.getLenders(response, loanId);
    }

    @POST
    @Path("/{id}/lenders/disperse")
    @Produces(MediaType.APPLICATION_JSON)
    public void calculateRepayments(@Suspended final AsyncResponse response,
                                    @PathParam("id") final String loanId,
                                    final LenderPayment lenderPayment) {

        service.calculateRepayments(response, loanId,lenderPayment);
    }

    @POST
    @Path("/{loan-id}/lenders/{lender-id}/payment")
    @Produces(MediaType.APPLICATION_JSON)
    public void makePayment(@Suspended final AsyncResponse response,
                            @PathParam("loan-id") final String loanId,
                            @PathParam("lender-id") final String lenderId,
                            final LenderPayment lenderPayment) {

        service.makePayment(response, loanId,lenderId,lenderPayment);
    }

    @GET
    @Path("/{id}/lenders/{lender-id}/payments")
    @Produces(MediaType.APPLICATION_JSON)
    public void getPayments(@Suspended final AsyncResponse response,
                            @PathParam("id") final String loanId,
                            @PathParam("lender-id") final String lenderId) {

        service.getPayments(response, loanId,lenderId);
    }

    @GET
    @Path("/{id}/lenders/{lender-id}/schedules")
    @Produces(MediaType.APPLICATION_JSON)
    public void getSchedule(@Suspended AsyncResponse response,
                            @PathParam("id") final String loanId,
                            @PathParam("lender-id") final String lenderId) {

        service.getSchedule(response, loanId,lenderId);
    }

}

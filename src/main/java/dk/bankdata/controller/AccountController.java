package dk.bankdata.controller;

import dk.bankdata.exception.AccountNotFoundException;
import dk.bankdata.exception.IncorrectBalanceFormatException;
import dk.bankdata.service.AccountService.IAccountService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    @Inject
    private IAccountService accountService;

    @GET
    @Path("{id}")
    public Response getAccountBalance(Long id) throws AccountNotFoundException {
        var balance = accountService.getAccountBalance(id);
        return Response.status(Response.Status.OK).entity(balance).build();
    }

    @PUT
    @Path("{id}")
    public Response depositToAccount(Long id, @RestQuery double amountToDeposit) throws AccountNotFoundException {
        accountService.depositToAccount(id, amountToDeposit);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @PUT
    @Path("transfer")
    public Response transferAmount(@RestQuery Long fromId, @RestQuery Long toId, @RestQuery double amountToTransfer) throws AccountNotFoundException, IncorrectBalanceFormatException {
        accountService.transferAmount(fromId, toId, amountToTransfer);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @POST
    @Operation(summary = "Create a new account", description = "Returns account number for the created resource.")
    public Response createAccount(@RestQuery String firstName, @RestQuery String lastName, @RestQuery double balance) {
        var accountId = accountService.createAccount(firstName, lastName, balance);
        return Response.status(Response.Status.CREATED).entity(accountId).build();
    }
}

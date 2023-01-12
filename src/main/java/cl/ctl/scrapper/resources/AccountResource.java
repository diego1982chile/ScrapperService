package cl.ctl.scrapper.resources;


import cl.ctl.scrapper.models.Account;
import cl.ctl.scrapper.services.AccountService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by des01c7 on 12-12-19.
 */
@RequestScoped
@Produces(APPLICATION_JSON)
@Path("accounts")
@RolesAllowed({"ADMIN","USER"})
public class AccountResource {

    @Inject
    AccountService accountService;

    static private final Logger logger = Logger.getLogger(AccountService.class.getName());

    @GET
    public Response getAllAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            return Response.ok(accounts).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @GET
    @Path("{retailer}/{holding}")
    public Response getAccountByClientAndHolding(@PathParam("retailer") String retailer, @PathParam("holding") String holding) {
        try {
            Account account = accountService.getAccountByRetailerAndHolding(retailer, holding);
            return Response.ok(account).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @POST
    @Path("save")
    public Response createAccount(Account account) {
        try {
            Account newAccount = accountService.saveAccount(account);
            return Response.ok(newAccount).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteAccount(@PathParam("id") long id) {
        try {
            accountService.deleteAccount(id);
            return Response.ok(id).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }


}

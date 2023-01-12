package cl.ctl.scrapper.resources;


import cl.ctl.scrapper.models.Holding;
import cl.ctl.scrapper.models.Retailer;
import cl.ctl.scrapper.services.AccountService;
import cl.ctl.scrapper.services.HoldingService;
import cl.ctl.scrapper.services.RetailerService;

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
@Path("holdings")
@RolesAllowed({"ADMIN","USER"})
public class HoldingResource {

    @Inject
    HoldingService holdingService;

    static private final Logger logger = Logger.getLogger(AccountService.class.getName());

    @GET
    public Response getAllHoldings() {
        try {
            List<Holding> retailers = holdingService.getAllRetailers();
            return Response.ok(retailers).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @POST
    @Path("save")
    public Response createHolding(Holding holding) {
        try {
            Holding newHolding = holdingService.saveHolding(holding);
            return Response.ok(newHolding).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteHolding(@PathParam("id") long id) {
        try {
            holdingService.deleteHolding(id);
            return Response.ok(id).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

}

package cl.ctl.scrapper.resources;


import cl.ctl.scrapper.models.Retailer;
import cl.ctl.scrapper.services.AccountService;
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
@Path("retailers")
@RolesAllowed({"ADMIN","USER"})
public class RetailerResource {

    @Inject
    RetailerService retailerService;

    static private final Logger logger = Logger.getLogger(AccountService.class.getName());

    @GET
    public Response getAllRetailers() {
        try {
            List<Retailer> retailers = retailerService.getAllRetailers();
            return Response.ok(retailers).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @POST
    @Path("save")
    public Response createRetailer(Retailer retailer) {
        try {
            Retailer newRetailer = retailerService.saveRetailer(retailer);
            return Response.ok(newRetailer).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteRetailer(@PathParam("id") long id) {
        try {
            retailerService.deleteRetailer(id);
            return Response.ok(id).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

}

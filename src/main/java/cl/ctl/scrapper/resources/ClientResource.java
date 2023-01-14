package cl.ctl.scrapper.resources;


import cl.ctl.scrapper.models.Account;
import cl.ctl.scrapper.models.Client;
import cl.ctl.scrapper.models.Retailer;
import cl.ctl.scrapper.services.AccountService;
import cl.ctl.scrapper.services.ClientService;
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
@Path("clients")
@RolesAllowed({"ADMIN","USER"})
public class ClientResource {

    @Inject
    ClientService clientService;

    static private final Logger logger = Logger.getLogger(ClientResource.class.getName());

    @GET
    public Response getAllClients() {
        try {
            List<Client> clients = clientService.getAllClients();
            return Response.ok(clients).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @POST
    @Path("save")
    public Response createClient(Client client) {
        try {
            Client newClient = clientService.saveClient(client);
            return Response.ok(newClient).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteClient(@PathParam("id") long id) {
        try {
            clientService.deleteClient(id);
            return Response.ok(id).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

}

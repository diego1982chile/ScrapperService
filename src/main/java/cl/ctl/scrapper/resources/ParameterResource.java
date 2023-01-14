package cl.ctl.scrapper.resources;

import cl.ctl.scrapper.models.Parameter;
import cl.ctl.scrapper.services.AccountService;
import cl.ctl.scrapper.services.ParameterService;

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
@Path("parameters")
@RolesAllowed({"ADMIN","USER"})
public class ParameterResource {

    @Inject
    ParameterService parameterService;

    static private final Logger logger = Logger.getLogger(AccountService.class.getName());

    @GET
    public Response getAllParameters() {
        try {
            List<Parameter> parameters = parameterService.getAllParameters();
            return Response.ok(parameters).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @POST
    @Path("save")
    public Response createParameter(Parameter parameter) {
        try {
            Parameter newHolding = parameterService.saveParameter(parameter);
            return Response.ok(newHolding).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteParameter(@PathParam("id") long id) {
        try {
            parameterService.deleteParameter(id);
            return Response.ok(id).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

}

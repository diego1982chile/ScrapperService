package cl.ctl.scrapper.services;

import cl.ctl.scrapper.helpers.ProcessHelper;
import cl.ctl.scrapper.managers.ScrapperManager;

import javax.ejb.Asynchronous;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by des01c7 on 12-12-19.
 */
@RequestScoped
@Produces(APPLICATION_JSON)
@Path("scrap")
public class ScrapService {

    @Inject
    ScrapperManager scrapperManager;

    static private final Logger logger = Logger.getLogger(ScrapService.class.getName());

    @POST
    @Asynchronous
    public Response scrap(@QueryParam("process") String process, @QueryParam("chains") List<String> chains) {
        try {
            scrapperManager.scrap(process, chains);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            return Response.ok("Proceso '" + ProcessHelper.getInstance().getProcessDate().format(dtf) + "' completado correctamente").build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

}

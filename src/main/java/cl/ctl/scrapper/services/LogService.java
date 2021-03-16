package cl.ctl.scrapper.services;

import cl.ctl.scrapper.helpers.LogHelper;
import cl.ctl.scrapper.helpers.ProcessHelper;
import cl.ctl.scrapper.managers.ScrapperManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ejb.Asynchronous;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
@Path("log")
public class LogService {

    static private final Logger logger = Logger.getLogger(LogService.class.getName());

    @GET
    public Response log() {
        try {
            String resp = getPrettyGson().toJson(LogHelper.getInstance().getLogs());
            return Response.ok(resp).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }


    private Gson getPrettyGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

}

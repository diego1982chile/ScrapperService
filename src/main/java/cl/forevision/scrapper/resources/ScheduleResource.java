package cl.forevision.scrapper.resources;


import cl.forevision.scrapper.models.Schedule;
import cl.forevision.scrapper.services.AccountService;
import cl.forevision.scrapper.services.ScheduleService;

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
@Path("schedules")
@RolesAllowed({"ADMIN","USER"})
public class ScheduleResource {

    @Inject
    ScheduleService scheduleService;

    static private final Logger logger = Logger.getLogger(AccountService.class.getName());

    @GET
    public Response getAllSchedules() {
        try {
            List<Schedule> parameters = scheduleService.getAllSchedules();
            return Response.ok(parameters).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @GET
    @Path("{retailer}")
    public Response getSchedulesByRetailer(@PathParam("retailer") String retailer) {
        try {
            List<Schedule> parameters = scheduleService.getSchedulesByRetailer(retailer);
            return Response.ok(parameters).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @POST
    @Path("save")
    public Response createSchedule(Schedule schedule) {
        try {
            Schedule newSchedule = scheduleService.saveSchedule(schedule);
            return Response.ok(newSchedule).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteSchedule(@PathParam("id") long id) {
        try {
            scheduleService.deleteParameter(id);
            return Response.ok(id).build();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Response.serverError().build();
    }

}

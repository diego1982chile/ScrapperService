package cl.forevision.scrapper.resources;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by root on 09-12-22.
 */
@RequestScoped
@Path("healthcheck")
public class HealthcheckResource implements HealthCheck {

    @GET
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("Application started").up().build();
    }


}

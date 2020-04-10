package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.bus.Communication;
import ch.mrnjec.swat.bus.Response;
import ch.mrnjec.swat.bus.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Controller("/api/v1/group")
public class GroupController {

    private static final String ROUTE_GROUP_LIST = "group.list";

    private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);

    Communication communication;

    GroupController(Communication communication) { this.communication = communication; }

    @Get("/")
    public HttpResponse<?> getGroupList() {
        try {
            String json = communication.syncCall(ROUTE_GROUP_LIST, "");
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(json, Response.class);
            if (response.getStatus() == Status.OK) {
                return HttpResponse.ok(response.getData());
            } else {
                return HttpResponse.serverError("Internal Server error");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return HttpResponse.serverError("Internal Server error");
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
            return HttpResponse.serverError("Internal Server error");
        }
    }

}

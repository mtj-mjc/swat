package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.bus.Communication;
import ch.mrnjec.swat.bus.Response;
import ch.mrnjec.swat.micro.filters.Filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;

import java.io.IOException;

public class ServiceCaller {

    public HttpResponse<Object> callService(Communication rabbitMQCommunication, Filter filter, String route) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String filterJson = mapper.writeValueAsString(filter);
        String json = rabbitMQCommunication.syncCall(route, filterJson);
        Response response = mapper.readValue(json, Response.class);
        return getHttpResponse(response);
    }

    public HttpResponse<Object> callService(Communication rabbitMQCommunication, String route, String message) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String json = rabbitMQCommunication.syncCall(route, message);
        Response response = mapper.readValue(json, Response.class);
        return getHttpResponse(response);
    }

    private HttpResponse<Object> getHttpResponse(Response response){
        return HttpResponse.status(HttpStatus.valueOf(response.getStatus().getValue()), response.getReason()).body(response.getData());
    }
}

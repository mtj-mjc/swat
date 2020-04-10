package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.bus.Communication;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/v1/store")
public class StoreController {

    private static final Logger LOG = LoggerFactory.getLogger(StoreController.class);

    private static final String ROUTE_STORE_LIST = "store.list";

    Communication rabbitMQCommunication;


    StoreController(Communication communication) {
        this.rabbitMQCommunication = communication;
    }

    @Get("/")
    public HttpResponse<?> getOrderList() {
        try {
            ServiceCaller caller = new ServiceCaller();
            return caller.callService(rabbitMQCommunication, ROUTE_STORE_LIST, "");
        } catch (Exception e) {
            return HttpResponse.serverError("Ein Fehler ist  aufgetreten");
        }
    }


}

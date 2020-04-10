package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.bus.Communication;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/v1/product")
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private static final String ROUTE_PRODUCT_LIST = "product.list";

    Communication communication;


    ProductController(Communication communication) {
        this.communication = communication;
    }

    @Get("/")
    public HttpResponse<?> getOrderList() {
        try {
            ServiceCaller caller = new ServiceCaller();
            return caller.callService(communication, ROUTE_PRODUCT_LIST, "");
        } catch (Exception e) {
            return HttpResponse.serverError("Ein Fehler ist  aufgetreten");
        }
    }
}

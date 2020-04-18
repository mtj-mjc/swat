
package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.bus.Communication;
import ch.mrnjec.swat.bus.Response;
import ch.mrnjec.swat.bus.Status;
import ch.mrnjec.swat.entities.Order;
import ch.mrnjec.swat.micro.filters.OrderFilter;
import ch.mrnjec.swat.micro.filters.OrderFilterField;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Optional;

/**
 * Controller für Customers.
 */
@Controller("/api/v1/order")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private static final String ROUTE_ORDER_LIST = "order.list";
    private static final String ROUTE_ORDER_CREATE = "order.create";


    Communication communication;


    OrderController(Communication communication) {
        this.communication = communication;
    }
    /**
     * Bestellung mit Id lesen.
     *
     * @param id Id der Bestellung.
     * @return Bestellung.
     */
    @Get("/{id}")
    public Order get(final long id) {
        // ToDo: Get Order by ID
        final Order order = new Order();
        LOG.info("REST: Student {} {}.", id, "geliefert");
        return order;
    }

    @Get("/")
    public HttpResponse<Object> getOrderList() {
        try {
            OrderFilter filter = new OrderFilter(OrderFilterField.NO_FILTER, "");
            ServiceCaller caller = new ServiceCaller();
            return caller.callService(communication, filter, ROUTE_ORDER_LIST);
        } catch (Exception e) {
            return HttpResponse.serverError(ErrorMessage.GENERIC_ERROR.getMessage());
        }
    }

    @Get("/user{?username,state}")
    public HttpResponse<Object> getOrderListByUser(final @Nullable String username, final Optional<Integer> state) {
        try {
            OrderFilter filter;
            if(!state.isEmpty() && state.get() == 0) {
                filter = new OrderFilter(OrderFilterField.COMPLETED_BY_USER, username);
            }
            else {
                filter = new OrderFilter(OrderFilterField.USERNAME, username);
            }
            ServiceCaller caller = new ServiceCaller();
            return caller.callService(communication, filter, ROUTE_ORDER_LIST);
        } catch (Exception e) {
            return HttpResponse.serverError(ErrorMessage.GENERIC_ERROR.getMessage());
        }
    }

    @Get("/state/{state}")
    public HttpResponse<Object> getOrderListByStatus(final String state) {
        try {
            OrderFilter filter = new OrderFilter(OrderFilterField.STATUS, state);
            ServiceCaller caller = new ServiceCaller();
            return caller.callService(communication, filter, ROUTE_ORDER_LIST);
        } catch (Exception e) {
            return HttpResponse.serverError(ErrorMessage.GENERIC_ERROR.getMessage());
        }
    }


    @Put("/")
    public HttpResponse<Object> createOrder(@Body String body){
        try {
            String responseJson = this.communication.syncCall(ROUTE_ORDER_CREATE, body);
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseJson, Response.class);
            if (response.getStatus() == ch.mrnjec.swat.bus.Status.OK) {
                return HttpResponse.ok("Order erfolgreich erstellt");
            }
            else if (response.getStatus() == Status.PRODUCTS_NOT_AVAILABLE){

                return HttpResponse.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Products not existing or not available").body(response.getData());
            }
            else {
                LOG.error(response.getData());
                return HttpResponse.serverError(ErrorMessage.GENERIC_ERROR.getMessage());
            }
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
            Thread.currentThread().interrupt();
            return HttpResponse.serverError(ErrorMessage.GENERIC_ERROR.getMessage());
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return HttpResponse.serverError(ErrorMessage.GENERIC_ERROR.getMessage());
        }
    }
}

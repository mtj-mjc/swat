package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.bus.BusConnector;
import ch.mrnjec.swat.bus.Response;
import ch.mrnjec.swat.bus.Status;
import ch.mrnjec.swat.entities.Order;
import ch.mrnjec.swat.entities.OrderPosition;
import ch.mrnjec.swat.entities.Product;
import ch.mrnjec.swat.entities.State;
import ch.mrnjec.swat.micro.filters.OrderFilter;
import ch.mrnjec.swat.micro.filters.OrderFilterField;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

class ServiceTest {
    private static final String CUSTOMER = "customer";
    private static final String STORE = "store";
    private static final String REPLY_TO = "replyTo";
    private static final String CORRELATION_ID = "correlationId";
    private static final String USER = "user";
    private static final String APPE = "appe";


    @Test
    public void testOnOrderCreate() throws IOException, TimeoutException {
        BusConnector bus = mock(BusConnector.class);
        OrderService orderService = mock(OrderService.class);
        doNothing().when(orderService).createOrder(any(Order.class));
        Service service = new Service(bus, orderService);
        Order order = new Order(new ObjectId(), new ArrayList<OrderPosition>(), USER, CUSTOMER, STORE, new Date(), State.PENDING);
        ObjectMapper mapper = new ObjectMapper();
        String orderJson = mapper.writeValueAsString(order);
        service.onOrderCreate(orderJson, REPLY_TO, CORRELATION_ID);
        verify(bus, times(1)).talkAsync(APPE, "order.created", orderJson);
    }

    @Test
    public void testOnOrderCreateException() throws IOException, TimeoutException {
        BusConnector bus = mock(BusConnector.class);
        OrderService orderService = mock(OrderService.class);
        doThrow(new IOException()).when(orderService).createOrder(any(Order.class));
        Service service = new Service(bus, orderService);
        Order order = new Order(new ObjectId(), new ArrayList<OrderPosition>(), USER, CUSTOMER, STORE, new Date(), State.PENDING);
        ObjectMapper mapper = new ObjectMapper();
        String orderJson = mapper.writeValueAsString(order);
        service.onOrderCreate(orderJson, REPLY_TO, CORRELATION_ID);
        Response response = new Response(Status.BAD_REQUEST, "", "Order data is not correct");
        String responseJson = mapper.writeValueAsString(response);
        verify(bus, times(1)).talkAsync(APPE, REPLY_TO, responseJson, CORRELATION_ID);
    }

    @Test
    public void testOnOrderList() throws IOException, TimeoutException {
        BusConnector bus = mock(BusConnector.class);
        OrderService orderService = mock(OrderService.class);
        OrderPosition position = new OrderPosition(new Product(new ObjectId(), "product", "desc", 55.0, "sortiment", "dfd"), 4);
        List<OrderPosition> positions = new ArrayList<>();
        positions.add(position);
        Order order = new Order(new ObjectId(), positions, USER, CUSTOMER, STORE, new Date(), State.PENDING);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        ObjectMapper mapper = new ObjectMapper();
        OrderFilter filter = new OrderFilter(OrderFilterField.NO_FILTER, "");
        when(orderService.getOrders(filter)).thenReturn(orders);
        String filterJson = mapper.writeValueAsString(filter);

        Service service = new Service(bus, orderService);
        service.onOrderList(filterJson, REPLY_TO, CORRELATION_ID);
        String responseOrderJson = mapper.writeValueAsString(orders);
        Response response = new Response(Status.OK, responseOrderJson, "");
        String responseJson = mapper.writeValueAsString(response);
        verify(bus, times(1)).talkAsync(APPE, REPLY_TO, responseJson, CORRELATION_ID);
    }
}
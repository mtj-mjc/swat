package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.bus.*;
import ch.mrnjec.swat.entities.Order;
import ch.mrnjec.swat.micro.filters.OrderFilter;
import ch.mrnjec.swat.micro.responses.ProductsUnavailableResponseContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public final class Service implements AutoCloseable {
    private static final Logger LOG = LogManager.getLogger(Service.class);

    private static final String ROUTE_ORDER_CREATE = "order.create";
    private static final String ROUTE_ORDER_LIST = "order.list";
    private static final String ROUTE_PRODUCT_HELLO = "product.hello";
    private static final String ROUTE_ORDER_CREATED = "order.created";
    private static final String ROUTE_PRODUCTS_BOOKED = "products.booked";
    private static final String ROUTE_PRODUCTS_UNAVAILABLE = "products.unavailable";

    private final String exchangeName;
    private final BusConnector bus;
    private final List<CreateOrderRequest> createRequests;
    private final OrderService orderService;

    /**
     * @throws IOException
     * @throws TimeoutException
     */
    Service() throws IOException, TimeoutException {
        this(new BusConnector(), new OrderService());
    }

    Service(BusConnector bus, OrderService orderService) throws IOException, TimeoutException {
        this.bus = bus;
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus.connect();
        this.createRequests = new ArrayList<>();
        this.orderService = orderService;
        this.registerRoute(ROUTE_ORDER_CREATE, this::onOrderCreate);
        this.registerRoute(ROUTE_ORDER_LIST, this::onOrderList);
        this.registerRoute(ROUTE_PRODUCT_HELLO, this::onProductHello);
        this.registerRoute(ROUTE_PRODUCTS_BOOKED, this::onProductsBooked);
        this.registerRoute(ROUTE_PRODUCTS_UNAVAILABLE, this::onProductsUnavailable);
    }

    private void onProductHello(String message, String replyTo, String correlationId) throws IOException {
        System.out.println(message);
    }

    /**
     * @throws IOException
     */
    private void registerRoute(String route, MessageReceiver receiver) throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", route);
        bus.listenFor(exchangeName, "Service | " + route, route, receiver);
    }

    /**
     * Returns orders based on the passed filter
     * @param filter Filter of type {@link OrderFilter}
     * @param replyTo Reply-to address
     * @param correlationId correlation Id for listener service
     * @throws IOException
     */
    public void onOrderList(String filter, String replyTo, String correlationId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderFilter orderfilter = mapper.readValue(filter, OrderFilter.class);
            List<Order> orders = this.orderService.getOrders(orderfilter);
            String ordersJson = mapper.writeValueAsString(orders);
            Response response = new Response(Status.OK, ordersJson, "");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, replyTo, responseJson, correlationId);
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
            Response response = new Response(Status.ERROR, "", "Error retrieving data");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, replyTo, responseJson, correlationId);
        }
    }

    public void onOrderCreate(String message, String replyTo, String correlationId) throws IOException {
        CreateOrderRequest request = null;
        try {

            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(message, Order.class);
            request = new CreateOrderRequest(correlationId, replyTo, order);
            createRequests.add(request);
            this.orderService.createOrder(order);
            String orderJson = mapper.writeValueAsString(order);
            bus.talkAsync(this.exchangeName, this.ROUTE_ORDER_CREATED, orderJson);
        } catch (IOException ex) {
            if (request != null) {
                createRequests.remove(request);
            }
            Response response = new Response(Status.BAD_REQUEST, "", "Order data is not correct");
            ObjectMapper mapper = new ObjectMapper();
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, replyTo, responseJson, correlationId);
        }
    }

    /**
     * Returns ok to Gateway
     *
     * @param id            contains only the Order Id
     * @param replyTo       empty
     * @param correlationId empty
     * @throws IOException
     */
    public void onProductsBooked(String id, String replyTo, String correlationId) throws IOException {
        CreateOrderRequest request = this.createRequests.stream()
                .filter(orderRequest -> id.equals(orderRequest.getOrderId().toString()))
                .findAny()
                .orElse(null);

        if (request == null) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.orderService.completeOrder(id);
        } catch (IOException ex) {
            Response response = new Response(Status.ERROR, "", "An error occured setting the order to ordered");
            LOG.error("An error occured setting the order to ordered");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, request.getReplyTo(), responseJson, request.getCorrelationId());
            this.createRequests.remove(request);
        }

        Response response = new Response(Status.OK, "", "Order successfully created");
        String responseJson = mapper.writeValueAsString(response);
        bus.talkAsync(this.exchangeName, request.getReplyTo(), responseJson, request.getCorrelationId());
        this.createRequests.remove(request);

    }

    /**
     * Sends "products-unavailable-error" to the gateway
     *
     * @param message       typeof {@link ProductsUnavailableResponseContent}
     * @param replyTo       null
     * @param correlationId null
     * @throws IOException
     */
    public void onProductsUnavailable(String message, String replyTo, String correlationId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Response errorResponse = mapper.readValue(message, Response.class);
        ProductsUnavailableResponseContent responseContent = mapper.readValue(errorResponse.getData(), ProductsUnavailableResponseContent.class);
        CreateOrderRequest request = this.createRequests.stream()
                .filter(orderRequest -> responseContent.getOrderId().equals(orderRequest.getOrderId()))
                .findAny()
                .orElse(null);

        if (request == null) {
            return;
        }
        try {
            this.orderService.cancelOrder(responseContent.getOrderId().toString());
        } catch (IOException ex) {
            Response response = new Response(Status.ERROR, "", "An error occured setting the order to canceled");
            LOG.error("An error occured setting the order to canceled");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, request.getReplyTo(), responseJson, request.getCorrelationId());
            this.createRequests.remove(request);
        }

        Response response = new Response(Status.PRODUCTS_NOT_AVAILABLE, message, "Products not available or not existing");

        String responseJson = mapper.writeValueAsString(response);
        bus.talkAsync(this.exchangeName, request.getReplyTo(), responseJson, request.getCorrelationId());
        this.createRequests.remove(request);

    }

    @Override
    public void close() throws Exception {
        bus.close();
    }
}

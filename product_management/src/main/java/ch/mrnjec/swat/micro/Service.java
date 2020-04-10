package ch.mrnjec.swat.micro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import ch.mrnjec.swat.bus.*;
import ch.mrnjec.swat.entities.Order;
import ch.mrnjec.swat.entities.OrderPosition;
import ch.mrnjec.swat.entities.Product;
import ch.mrnjec.swat.micro.responses.ProductsUnavailableResponseContent;
import ch.mrnjec.swat.mongo.AdapterFactory;
import ch.mrnjec.swat.mongo.ProductAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class Service implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(Service.class);
    private final String exchangeName;
    private final BusConnector bus;
    private AdapterFactory adapterFactory;

    private static final String ROUTE_PRODUCT_HELLO = "product.hello";
    private static final String ROUTE_ORDER_CREATED = "order.created";
    private static final String ROUTE_PRODUCTS_BOOKED = "products.booked";
    private static final String ROUTE_PRODUCTS_UNAVAILABLE = "products.unavailable";
    private static final String ROUTE_PRODUCTS_LIST = "product.list";

    /**
     * @throws IOException
     * @throws TimeoutException
     */
    Service() throws IOException, TimeoutException {
        this(new BusConnector());
    }

    Service(BusConnector bus) throws IOException, TimeoutException {
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = bus;
        this.bus.connect();
        this.registerRoute(ROUTE_ORDER_CREATED, this::onOrderCreated);
        this.registerRoute(ROUTE_PRODUCTS_LIST, this::onProductsList);
        this.adapterFactory = new AdapterFactory();
    }

    private void onProductsList(String message, String replyTo, String correlationId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductService service = new ProductService();
            List<Product> products = service.getAllProducts();
            String productsJson = mapper.writeValueAsString(products);
            Response response = new Response(Status.OK, productsJson, "");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(exchangeName, replyTo, responseJson, correlationId);
        } catch (Exception e) {
            Response response = new Response(Status.ERROR, "", "Internal Server Error");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(exchangeName, replyTo, responseJson, correlationId);
            LOG.error(e.getMessage());
        }
    }

    /**
     * @throws IOException
     */
    private void registerRoute(String route, MessageReceiver receiver) throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", route);
        bus.listenFor(exchangeName, "Service | " + route, route, receiver);
    }

    /**
     * Sendet einen Hello World Event.
     *
     * @throws IOException IOException.
     */
    public void sendHelloWorld() throws IOException {
        LOG.debug("Hello World");
        bus.talkAsync(exchangeName, ROUTE_PRODUCT_HELLO, "Hello World");
    }

    public void onOrderCreated(String message, String replyTo, String correlationId) throws IOException {
        ProductAdapter productAdapter = (ProductAdapter) this.adapterFactory.getAdapter(Product.class);
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(message, Order.class);
        List<OrderPosition> orderPositions = order.getPositions();
        List<Product> unavailableProducts = new ArrayList<>();
        List<Product> nonExistingProducts = new ArrayList<>();
        for (OrderPosition orderedProduct : orderPositions) {
            try {
                Product productInStorage = productAdapter.getById(orderedProduct.getProduct().getId().toString());
                if (orderedProduct.getCount() > productInStorage.getCount()) {
                    unavailableProducts.add(orderedProduct.getProduct());
                }
            } catch (NoSuchElementException ex) {
                nonExistingProducts.add(orderedProduct.getProduct());
            }
        }

        Response response;
        if (unavailableProducts.size() > 0 || nonExistingProducts.size() > 0) {
            ProductsUnavailableResponseContent responseContent = new ProductsUnavailableResponseContent(order.getId());
            responseContent.setUnavailableProducts(unavailableProducts);
            responseContent.setNonExistingProducts(nonExistingProducts);
            String jsonContent = mapper.writeValueAsString(responseContent);
            response = new Response(Status.ERROR, jsonContent, "Products not available or not existing");
            String jsonResponse = mapper.writeValueAsString(response);
            bus.talkAsync(exchangeName, ROUTE_PRODUCTS_UNAVAILABLE, jsonResponse);
        }
        else {
            ProductService service = new ProductService();
            service.bookProducts(orderPositions);
            bus.talkAsync(exchangeName, ROUTE_PRODUCTS_BOOKED, order.getId().toString());
        }
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}

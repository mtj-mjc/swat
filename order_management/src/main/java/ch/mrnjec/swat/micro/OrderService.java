package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.entities.Order;
import ch.mrnjec.swat.entities.State;
import ch.mrnjec.swat.micro.filters.OrderFilter;
import ch.mrnjec.swat.mongo.AdapterFactory;
import ch.mrnjec.swat.mongo.OrderAdapter;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final AdapterFactory adapterFactory;
    public static final String USERNAME_FIELD = "userName";
    public static final String STAUS_FIELD = "state";

    public OrderService() {
        this.adapterFactory = new AdapterFactory();
    }


    public void createOrder(Order order) throws IOException {
        order.setState(State.PENDING);
        order.setId(new ObjectId());
        OrderAdapter adapter = (OrderAdapter) this.adapterFactory.getAdapter(Order.class);
        adapter.create(order);
    }

    public List<Order> getAllOrders() throws IOException {
        OrderAdapter adapter = (OrderAdapter) this.adapterFactory.getAdapter(Order.class);
        List<Order> orders = adapter.getAll();
        return orders;
    }

    public void completeOrder(String id) throws IOException {
        OrderAdapter adapter = (OrderAdapter) this.adapterFactory.getAdapter(Order.class);
        adapter.completeOrder(id);
    }

    public void cancelOrder(String id) throws IOException {
        OrderAdapter adapter = (OrderAdapter) this.adapterFactory.getAdapter(Order.class);
        adapter.cancelOrder(id);
    }

    public List<Order> getOrdersByUser(String username) throws IOException {
        OrderAdapter adapter = (OrderAdapter) this.adapterFactory.getAdapter(Order.class);
        return adapter.getBy(Filters.eq(USERNAME_FIELD, username));
    }
    public List<Order> getOrdersByStatus(int statecode) throws IOException {
        OrderAdapter adapter = (OrderAdapter) this.adapterFactory.getAdapter(Order.class);
        return adapter.getBy(Filters.eq(STAUS_FIELD, statecode));
    }

    public List<Order> getOrders(OrderFilter filter) throws IOException {
        switch (filter.getField()) {
            case USERNAME:
                return this.getOrdersByUser(filter.getContent());
            case STATUS:
                return this.getOrdersByStatus(Integer.parseInt(filter.getContent()));
            case NO_FILTER:
                return this.getAllOrders();
            default:
                return new ArrayList<>();
        }
    }
}

package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Order;
import ch.mrnjec.swat.entities.OrderPosition;
import ch.mrnjec.swat.entities.Product;
import ch.mrnjec.swat.entities.State;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Initialize User Management Database
 *
 * @since: 14.05.2019
 * @author: Matej Mrnjec
 */
public class MongoDBInit {

    public static void main(final String[] args) throws IOException {
        deleteDatabase(MongoDbConfig.DATABASE);
        // Only for Development
        createOrder();
    }

    private static void deleteDatabase(String name){
        MongoDbConfig config = new MongoDbConfig();
        try (MongoClient mongoClient = new MongoClient(new MongoClientURI(config.getConnectionAddress()))) {
            mongoClient.getDatabase(name).drop();
        }
    }

    public static void createOrder() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        OrderAdapter orderAdapter = (OrderAdapter) factory.getAdapter(Order.class);
        List<OrderPosition> positions = createPositions();
        Order order = new Order(new ObjectId(), positions, "flaz",
                new ObjectId().toString(), new ObjectId().toString(), new Date(), State.PENDING);
        orderAdapter.create(order);
    }

    public static List<Product> createProducts(){
        List<Product> products = new ArrayList<>();
        products.add(new Product(new ObjectId(), "iPhone 6", "Apple Smartphone der 6.Generation",
                549.99, new ObjectId().toString(), new ObjectId().toString()));
        products.add(new Product(new ObjectId(), "iPhone 7", "Apple Smartphone der 7.Generation",
                649.99, new ObjectId().toString(), new ObjectId().toString()));
        products.add(new Product(new ObjectId(), "Samsung A6", "Samsung Smartphone der 6.Generation A Serie",
                499.99, new ObjectId().toString(), new ObjectId().toString()));
        products.add(new Product(new ObjectId(), "Samsung S7", "Samsung Smartphone der 7.Generation S Serie",
                899.99, new ObjectId().toString(), new ObjectId().toString()));
        return products;
    }

    public static List<OrderPosition> createPositions(){
        List<Product> products = createProducts();
        List<OrderPosition> positions = new ArrayList<>();
        int i=0;
        for(Product product : products){
            positions.add(new OrderPosition(product, i));
            i++;
        }
        return positions;
    }
}

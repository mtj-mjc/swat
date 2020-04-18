package ch.mrnjec.swat.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @Test
    public void serialize() throws IOException {
        Product product = new Product(new ObjectId(), "test", "desc", 55.00, "dfd", "dfdf");
        OrderPosition position = new OrderPosition(product, 5 );
        List<OrderPosition> positions = new ArrayList<>();
        positions.add(position);
        Order order = new Order(new ObjectId(), positions, "user", "customer", "store", new Date(), State.PENDING);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);

        Order orderFromJson = mapper.readValue(json, Order.class);
        assertEquals(orderFromJson, order);
    }

}
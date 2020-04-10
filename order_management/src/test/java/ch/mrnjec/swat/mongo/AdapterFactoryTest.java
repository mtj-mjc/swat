package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test Class for {@link AdapterFactory}
 *
 * @since: 21.05.2019
 * @author: Matej Mrnjec
 */
class AdapterFactoryTest {

    /**
     * Test method for {@link AdapterFactory#getAdapter(Class)} .
     */
    @Test
    void getOrderAdapterTest() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        Class<Order> entity = Order.class;
        EntityAdapter adapter = factory.getAdapter(entity);
        assertTrue(adapter instanceof OrderAdapter);
        assertTrue(adapter.getMongoAdapter().getType().equals(entity));
    }
}
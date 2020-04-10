package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Category;
import ch.mrnjec.swat.entities.Product;
import ch.mrnjec.swat.entities.Sortiment;
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
        Class<Sortiment> entity = Sortiment.class;
        EntityAdapter adapter = factory.getAdapter(entity);
        assertTrue(adapter instanceof SortimentAdapter);
        assertTrue(adapter.getMongoAdapter().getType().equals(entity));
    }

    /**
     * Test method for {@link AdapterFactory#getAdapter(Class)} .
     */
    @Test
    void getProductAdapterTest() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        Class<Product> entity = Product.class;
        EntityAdapter adapter = factory.getAdapter(entity);
        assertTrue(adapter instanceof ProductAdapter);
        assertTrue(adapter.getMongoAdapter().getType().equals(entity));
    }

    /**
     * Test method for {@link AdapterFactory#getAdapter(Class)} .
     */
    @Test
    void getCategoryAdapterTest() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        Class<Category> entity = Category.class;
        EntityAdapter adapter = factory.getAdapter(entity);
        assertTrue(adapter instanceof CategoryAdapter);
        assertTrue(adapter.getMongoAdapter().getType().equals(entity));
    }
}
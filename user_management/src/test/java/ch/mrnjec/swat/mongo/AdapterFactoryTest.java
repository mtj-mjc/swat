package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Group;
import ch.mrnjec.swat.entities.Store;
import ch.mrnjec.swat.entities.User;
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
    void getUserAdapterTest() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        Class<User> entity = User.class;
        EntityAdapter adapter = factory.getAdapter(entity);
        assertTrue(adapter instanceof UserAdapter);
        assertTrue(adapter.getMongoAdapter().getType().equals(entity));
    }

    /**
     * Test method for {@link AdapterFactory#getAdapter(Class)} .
     */
    @Test
    void getGroupAdapterTest() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        Class<Group> entity = Group.class;
        EntityAdapter adapter = factory.getAdapter(entity);
        assertTrue(adapter instanceof GroupAdapter);
        assertTrue(adapter.getMongoAdapter().getType().equals(entity));
    }

    /**
     * Test method for {@link AdapterFactory#getAdapter(Class)} .
     */
    @Test
    void getStoreAdapterTest() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        Class<Store> entity = Store.class;
        EntityAdapter adapter = factory.getAdapter(entity);
        assertTrue(adapter instanceof StoreAdapter);
        assertTrue(adapter.getMongoAdapter().getType().equals(entity));
    }
}
package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Entity;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Abstract Text Class for all Classes which implement {@link EntityAdapter}.
 *
 * @since: 21.05.2019
 * @author: matej
 */
abstract public class EntityAdapterTest<T extends Entity> {
    /**
     * Gets a instance of Entity for Tests
     * @return Entity
     */
    abstract T getTestEntity();

    /**
     * Get List of Entities for Tests
     * @return List of Entities
     */
    abstract List<T> getTestEntities();

    /**
     * Gets AdapterClass which the Class wants to Test
     * @return
     */
    abstract Class<? extends EntityAdapter<T>> getAdapterClass();

    /**
     * Get instance of EntityAdapter with mocked MongoAdapter in it.
     * @param mockedMongoAdapter Mocked MongoAdapter
     * @return instance of EntityAdapter
     */
    private EntityAdapter<T> getTestEntityAdapter(MongoAdapter<T> mockedMongoAdapter){
        try {
            return getAdapterClass().getDeclaredConstructor(MongoAdapter.class).newInstance(mockedMongoAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get instance of EntityAdapter with all needed mocks for GetOne method.
     * @return EntityAdapter
     * @throws IOException
     */
    EntityAdapter<T> getAdapterForGetOneFailsTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(ArgumentMatchers.any(Bson.class))).thenThrow(new NoSuchElementException());

        // Test getByName Method in UserAdapter
        return getTestEntityAdapter(mongoAdapter);
    }

    /**
     * Get instance of EntityAdapter with all needed mocks for GetOneFails method.
     * @return EntityAdapter
     * @throws IOException
     */
    EntityAdapter<T> getAdapterForGetOneTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(ArgumentMatchers.any(Bson.class))).thenReturn(getTestEntity());

        // Test getByName Method in UserAdapter
        return getTestEntityAdapter(mongoAdapter);
    }

    /**
     * Get instance of EntityAdapter with all needed mocks for GetOne method.
     * @return EntityAdapter
     * @throws IOException
     */
    EntityAdapter<T> getAdapterForGetFailsTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.get(ArgumentMatchers.any(Bson.class))).thenReturn(Collections.emptyList());

        // Test getByName Method in UserAdapter
        return getTestEntityAdapter(mongoAdapter);
    }

    /**
     * Get instance of EntityAdapter with all needed mocks for GetOneFails method.
     * @return EntityAdapter
     * @throws IOException
     */
    EntityAdapter<T> getAdapterForGetTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.get(ArgumentMatchers.any(Bson.class))).thenReturn(getTestEntities());

        // Test getByName Method in UserAdapter
        return getTestEntityAdapter(mongoAdapter);
    }

    /**
     * Test method for {@link EntityAdapter#getAll()} .
     */
    @Test
    void getAllTest() throws IOException {
        List<T> testData = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            testData.add(getTestEntity());

        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getAll()).thenReturn(testData);

        // Test getAll Method in Adapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        List<T> results = adapter.getAll();
        assertTrue(results.equals(testData));
    }

    /**
     * Test method for {@link EntityAdapter#getAll()} .
     */
    @Test
    void getAllNoneTest() throws IOException {
        // Create Test Data
        List<T> testData = Collections.emptyList();

        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getAll()).thenReturn(testData);

        // Test getAll Method in GroupAdapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        List<T> results = adapter.getAll();
        assertTrue(results.equals(testData));
    }

    /**
     * Test method for exists(T) .
     */
    @Test
    void existsTest() throws IOException {
        // Create Test Data
        T testGroup = getTestEntity();

        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(ArgumentMatchers.any(Bson.class))).thenReturn(testGroup);

        // Test exists Method in Adapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        assertTrue(adapter.exists(testGroup));
    }

    /**
     * Test method for exists(T) .
     */
    @Test
    void existsNotTest() throws IOException {
        // Create Test Data
        T testGroup = getTestEntity();

        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(ArgumentMatchers.any(Bson.class))).thenThrow(new NoSuchElementException());

        // Test exists Method in Adapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        assertFalse(adapter.exists(testGroup));
    }

    /**
     * Test method for {@link EntityAdapter#create(Entity)} .
     */
    @Test
    void createTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(any(Bson.class))).thenThrow(new NoSuchElementException());

        // Test create Method in UserAdapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        adapter.create(getTestEntity());
        verify(mongoAdapter, times(1)).create(ArgumentMatchers.<T>any());
    }

    /**
     * Test method for {@link EntityAdapter#create(Entity)} .
     */
    @Test
    void createExistsAlreadyTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        // Test create Method in UserAdapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        adapter.create(getTestEntity());
        verify(mongoAdapter, times(0)).create(ArgumentMatchers.<T>any());
    }

    /**
     * Test method for {@link EntityAdapter#create(Entity)} .
     */
    @Test
    void createFailsTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(any(Bson.class))).thenThrow(new NoSuchElementException());
        doThrow(new IOException()).when(mongoAdapter).create(ArgumentMatchers.<T>any());

        // Test create Method in UserAdapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        assertThrows(IOException.class, () -> {
            adapter.create(getTestEntity());
        });
    }

    /**
     * Test method for {@link EntityAdapter#remove(Entity)} .
     */
    @Test
    void removeTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(any(Bson.class))).thenReturn(getTestEntity());
        when(mongoAdapter.remove(ArgumentMatchers.<T>any())).thenReturn(true);

        // Test remove Method in UserAdapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        adapter.remove(getTestEntity());
        verify(mongoAdapter, times(1)).remove(ArgumentMatchers.<T>any());
    }

    /**
     * Test method for {@link EntityAdapter#remove(Entity)} .
     */
    @Test
    void removeExistsNotTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(any(Bson.class))).thenThrow(new NoSuchElementException());

        // Test remove Method in UserAdapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        adapter.remove(getTestEntity());
        verify(mongoAdapter, times(0)).remove(ArgumentMatchers.<T>any());
    }

    /**
     * Test method for {@link EntityAdapter#remove(Entity)} .
     */
    @Test
    void removeFailsTest() throws IOException {
        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(any(Bson.class))).thenReturn(getTestEntity());
        when(mongoAdapter.remove(ArgumentMatchers.<T>any())).thenReturn(false);

        // Test remove Method in UserAdapter
        EntityAdapter<T> adapter = getTestEntityAdapter(mongoAdapter);
        assertThrows(IOException.class, () -> {
            adapter.remove(getTestEntity());
        });
    }
}

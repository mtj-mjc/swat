package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Entity;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Abstract Text Class for all Classes which implement {@link EntityAdapterExtended}.
 * Extends {@link EntityAdapterTest} because {@link EntityAdapterExtended} implements {@link EntityAdapter}.
 *
 * @since: 21.05.2019
 * @author: Matej Mrnjec
 */
public abstract class EntityAdapterExtendedTest<T extends Entity> extends EntityAdapterTest<T> {

    /**
     * Gets a ObjectId for Tests
     * @return ObjectId
     */
    abstract ObjectId getId();

    /**
     * Gets a Name for Tests
     * @return Name
     */
    abstract String getName();

    @Override
    abstract Class<? extends EntityAdapterExtended<T>> getAdapterClass();

    /**
     * Get instance of EntityAdapterExtended with mocked MongoAdapter in it.
     * @param mockedMongoAdapter Mocked MongoAdapter
     * @return instance of EntityAdapterExtended
     */
    private EntityAdapterExtended<T> getTestEntityAdapter(MongoAdapter<T> mockedMongoAdapter){
        try {
            return getAdapterClass().getDeclaredConstructor(MongoAdapter.class).newInstance(mockedMongoAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Test method for {@link EntityAdapterExtended#getById(String)} .
     */
    @Test
    void getById() throws IOException {
        // Create Test Data
        T testData = getTestEntity();

        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(ArgumentMatchers.any(Bson.class))).thenReturn(testData);

        // Test exists Method in Adapter
        EntityAdapterExtended<T> adapter = getTestEntityAdapter(mongoAdapter);
        assertTrue(testData.equals(adapter.getById(getId().toString())));
    }

    /**
     * Test method for {@link EntityAdapterExtended#getById(String)} .
     */
    @Test
    void getByIdFails() throws IOException {
        // Create Test Data
        T testGroup = getTestEntity();

        MongoAdapter<T> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(ArgumentMatchers.any(Bson.class))).thenThrow(new NoSuchElementException());

        // Test exists Method in Adapter
        EntityAdapterExtended<T> adapter = getTestEntityAdapter(mongoAdapter);
        assertThrows(NoSuchElementException.class, () -> {
            adapter.getById(getId().toString());
        });
    }
}

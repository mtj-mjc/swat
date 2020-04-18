package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Product;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Test Class for {@link ProductAdapter} .
 *
 * @since: 10.05.2019
 * @author: Matej Mrnjec
 */
class ProductAdapterTest extends EntityAdapterExtendedTest<Product> {
    public static final String TEST_STR = "Test";
    public static final String NOID = "000000000000000000000000";
    public static final ObjectId TEST_ID = new ObjectId(NOID);
    public static final double TEST_PRICE = 100.00;
    public static final int TEST_QUANTITY = 100;


    /**
     * Gets a ObjectId for Tests
     *
     * @return ObjectId
     */
    @Override
    ObjectId getId() {
        return TEST_ID;
    }

    /**
     * Gets a Name for Tests
     *
     * @return Name
     */
    @Override
    String getName() {
        return TEST_STR;
    }

    /**
     * Gets a instance of Entity for Tests
     *
     * @return Entity
     */
    @Override
    Product getTestEntity() {
        return new Product(TEST_ID,TEST_STR,TEST_STR,TEST_PRICE, TEST_STR, TEST_STR, TEST_QUANTITY);
    }

    /**
     * Get List of Entities for Tests
     *
     * @return List of Entities
     */
    @Override
    List<Product> getTestEntities() {
        return Arrays.asList(new Product(TEST_ID,TEST_STR,TEST_STR,TEST_PRICE, TEST_STR, TEST_STR, TEST_QUANTITY)
                ,new Product(TEST_ID,TEST_STR,TEST_STR,TEST_PRICE, TEST_STR, TEST_STR, TEST_QUANTITY));
    }

    @Override
    Class<? extends EntityAdapterExtended<Product>> getAdapterClass() {
        return ProductAdapter.class;
    }

    @Test
    void bookProductTest() throws IOException {
        MongoAdapter<Product> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(ArgumentMatchers.any(Bson.class))).thenReturn(getTestEntity());
        
        // Test update Method in UserAdapter
        ProductAdapter adapter = new ProductAdapter(mongoAdapter);
        adapter.bookProduct(getTestEntity(), 100);
        verify(mongoAdapter, times(1)).update(any(Bson.class), any(Bson.class));
    }

    @Test
    void bookProductFailsTest() throws IOException {
        MongoAdapter<Product> mongoAdapter = mock(MongoAdapter.class);
        doThrow(IOException.class).when(mongoAdapter).update(any(Bson.class), any(Bson.class));
        when(mongoAdapter.getOne(ArgumentMatchers.any(Bson.class))).thenReturn(getTestEntity());

        // Test update Method in UserAdapter
        ProductAdapter adapter = new ProductAdapter(mongoAdapter);
        assertThrows(IOException.class, () -> adapter.bookProduct(getTestEntity(), TEST_QUANTITY));
    }
}
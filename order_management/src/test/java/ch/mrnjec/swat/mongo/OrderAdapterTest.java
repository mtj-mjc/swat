package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Order;
import ch.mrnjec.swat.entities.OrderPosition;
import ch.mrnjec.swat.entities.State;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Class for {@link OrderAdapter}
 *
 * @since: 23.05.2019
 * @author: Matej Mrnjec
 */
class OrderAdapterTest extends EntityAdapterExtendedTest<Order> {
    public static final String TEST_STR = "Test";
    public static final Date TEST_DATE = new Date();
    public static final State TEST_STATE = State.NOT_AVAILABLE;
    public static final List<OrderPosition> TEST_ORDER_POSITIONS = new ArrayList<>();
    public final static String NOID = "000000000000000000000000";
    public static final ObjectId TEST_ID = new ObjectId(NOID);

    /**
     * Gets a ObjectId for Tests
     *
     * @return ObjectId
     */
    @Override
    ObjectId getId() {
        return new ObjectId();
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

    @Override
    Class<? extends EntityAdapterExtended<Order>> getAdapterClass() {
        return OrderAdapter.class;
    }

    @Override
    Order getTestEntity() {
        return new Order(TEST_ID,TEST_ORDER_POSITIONS,TEST_STR,TEST_STR, TEST_STR, TEST_DATE, TEST_STATE);
    }

    @Override
    List<Order> getTestEntities() {
        return Arrays.asList(new Order(TEST_ID,TEST_ORDER_POSITIONS,TEST_STR,TEST_STR, TEST_STR, TEST_DATE, TEST_STATE)
                ,new Order(TEST_ID,TEST_ORDER_POSITIONS,TEST_STR,TEST_STR, TEST_STR, TEST_DATE, TEST_STATE));
    }

    /**
     * Test method for {@link OrderAdapter#getByUserName(String)} .
     */
    @Test
    void getByUserNameTest() throws IOException {
        // Test getByName Method in UserAdapter
        OrderAdapter adapter = (OrderAdapter) super.getAdapterForGetTest();
        assertTrue(getTestEntities().equals(adapter.getByUserName(TEST_STR)));
    }

    /**
     * Test method for {@link OrderAdapter#getByUserName(String)} .
     */
    @Test
    void getByUserNameFailsTest() throws IOException {
        // Test getByName Method in UserAdapter
        OrderAdapter adapter = (OrderAdapter) super.getAdapterForGetFailsTest();
        assertTrue(adapter.getByStoreId(TEST_STR).equals(Collections.emptyList()));
    }

    /**
     * Test method for {@link OrderAdapter#getByState(State)} .
     */
    @Test
    void getByStateTest() throws IOException {
        // Test getByName Method in UserAdapter
        OrderAdapter adapter = (OrderAdapter) super.getAdapterForGetTest();
        assertTrue(getTestEntities().equals(adapter.getByState(TEST_STATE)));
    }

    /**
     * Test method for {@link OrderAdapter#getByState(State)} .
     */
    @Test
    void getByStateFailsTest() throws IOException {
        // Test getByName Method in UserAdapter
        OrderAdapter adapter = (OrderAdapter) super.getAdapterForGetFailsTest();
        assertTrue(adapter.getByStoreId(TEST_STR).equals(Collections.emptyList()));
    }

    /**
     * Test method for {@link OrderAdapter#getByStoreId(String)} .
     */
    @Test
    void getByStoreIdTest() throws IOException {
        // Test getByName Method in UserAdapter
        OrderAdapter adapter = (OrderAdapter) super.getAdapterForGetTest();
        assertTrue(getTestEntities().equals(adapter.getByStoreId(TEST_STR)));
    }

    /**
     * Test method for {@link OrderAdapter#getByStoreId(String)}.
     */
    @Test
    void getByStoreIdFailsTest() throws IOException {
        // Test getByName Method in UserAdapter
        OrderAdapter adapter = (OrderAdapter) super.getAdapterForGetFailsTest();
        assertTrue(adapter.getByStoreId(TEST_STR).equals(Collections.emptyList()));
    }

    /**
     * Test method for {@link OrderAdapter#getByCustomerId(String)} .
     */
    @Test
    void getByCustomerIdTest() throws IOException {
        // Test getByName Method in UserAdapter
        OrderAdapter adapter = (OrderAdapter) super.getAdapterForGetTest();
        assertTrue(getTestEntities().equals(adapter.getByCustomerId(TEST_STR)));
    }

    /**
     * Test method for {@link OrderAdapter#getByCustomerId(String)}.
     */
    @Test
    void getByCustomerIdFailsTest() throws IOException {
        // Test getByName Method in UserAdapter
        OrderAdapter adapter = (OrderAdapter) super.getAdapterForGetFailsTest();
        assertTrue(adapter.getByCustomerId(TEST_STR).equals(Collections.emptyList()));
    }

    /**
     * Test method for {@link OrderAdapter#update(Order)} .
     */
    @Test
    void updateTest() throws IOException {
        MongoAdapter<Order> mongoAdapter = mock(MongoAdapter.class);

        // Test update Method in UserAdapter
        OrderAdapter adapter = new OrderAdapter(mongoAdapter);
        adapter.update(getTestEntity());
        verify(mongoAdapter, times(1)).update(any(Bson.class), any(Order.class));
    }

    /**
     * Test method for {@link OrderAdapter#update(Order)} .
     */
    @Test
    void updateFailsTest() throws IOException {
        MongoAdapter<Order> mongoAdapter = mock(MongoAdapter.class);
        doThrow(IOException.class).when(mongoAdapter).update(any(Bson.class), any(Order.class));

        // Test update Method in UserAdapter
        OrderAdapter adapter = new OrderAdapter(mongoAdapter);
        assertThrows(IOException.class, () -> {
            adapter.update(getTestEntity());
        });
    }

    /**
     * Test method for {@link OrderAdapter#updateOrderState(String, State)} .
     */
    @Test
    void updateOrderStateTest() throws IOException {
        MongoAdapter<Order> mongoAdapter = mock(MongoAdapter.class);

        // Test update Method in UserAdapter
        OrderAdapter adapter = new OrderAdapter(mongoAdapter);
        adapter.updateOrderState(getTestEntity().getId().toString(),TEST_STATE);
        verify(mongoAdapter, times(1)).update(any(Bson.class), any(Bson.class));
    }

    /**
     * Test method for {@link OrderAdapter#updateOrderState(String, State)}  .
     */
    @Test
    void updateOrderStateFailsTest() throws IOException {
        MongoAdapter<Order> mongoAdapter = mock(MongoAdapter.class);
        when(mongoAdapter.getOne(any(Bson.class))).thenReturn(getTestEntity());
        doThrow(IOException.class).when(mongoAdapter).update(any(Bson.class), any(Bson.class));

        // Test update Method in UserAdapter
        OrderAdapter adapter = new OrderAdapter(mongoAdapter);
        assertThrows(IOException.class, () -> {
            adapter.updateOrderState(getTestEntity().getId().toString(), TEST_STATE);
        });
    }
}
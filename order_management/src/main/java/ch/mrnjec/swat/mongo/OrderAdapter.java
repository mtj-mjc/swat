package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Order;
import ch.mrnjec.swat.entities.State;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This Class is an Adapter to the MongoDB for the Entity Order.
 *
 * @since: 23.04.2019
 * @author: Matej Mrnjec
 */
public class OrderAdapter implements EntityAdapterExtended<Order> {
    public static final String ID_FIELD = "_id";
    public static final String USERNAME_FIELD = "userName";
    public static final String STOREID_FIELD = "storeid";
    public static final String CUSTOMERID_FIELD = "customerid";
    public static final String STATE_FIELD = "state";

    private MongoAdapter<Order> mongoAdapter;

    OrderAdapter(MongoAdapter mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
        this.mongoAdapter.changeCollection(MongoDbConfig.DATABASE, MongoDbConfig.ORDER_COLLECTION);
    }

    @Override
    public MongoAdapter<Order> getMongoAdapter() {
        return mongoAdapter;
    }


    /**
     * Checks if a Order already exists. It uses the getById Method to check if Order already exists.
     * @param order Order which has to be checked
     * @return If found then true else false
     */
    public Boolean exists(Order order){
        try{
            getById(order.getId().toString());
        }
        catch (NoSuchElementException ex){
            return false;
        }
        return true;
    }

    /**
     * Gets every saved Order which has been created by given User.
     * @param username username
     * @return List of found Order's that match with username
     */
    public List<Order> getByUserName(String username){
        return getBy(Filters.eq(USERNAME_FIELD, username));
    }

    /**
     * Gets every saved Order which has been ordered for given Store.
     * @param storeid storeid
     * @return List of found Order's that match with storeid
     */
    public List<Order> getByStoreId(String storeid){
        return getBy(Filters.eq(STOREID_FIELD, storeid));
    }

    /**
     * Gets every saved Order which has been ordered for given Customer.
     * @param customerid customerid
     * @return List of found Order's that match with customerid
     */
    public List<Order> getByCustomerId(String customerid){
        return getBy(Filters.eq(CUSTOMERID_FIELD, customerid));
    }

    /**
     * Gets every saved Order which has given State.
     * @param state state
     * @return List of found Order's that match with state
     */
    public List<Order> getByState(State state) {
        return getBy(Filters.eq(STATE_FIELD, state.getId()));
    }

    /**
     * Gets every saved Order which has given State and is ordered by user.
     * @param username user
     * @param state state
     * @return List of found Order's that match with state and user
     */
    public List<Order> getByUserAndState(String username, State state) {
        return getBy(Filters.and(Filters.eq(STATE_FIELD, state.getId()), Filters.eq(USERNAME_FIELD, username)));
    }

    /**
     * Update an Order.
     * @param order Updated Order
     * @throws IOException If convert fails in MongoAdapter or update was not acknowledged
     */
    public void update(Order order) throws IOException {
        this.mongoAdapter.update(Filters.eq(ID_FIELD, order.getId()), order);
    }

    /**
     * Updates the Field State of Order
     * @param id id of Order
     * @param state New State
     * @throws IOException If update was not acknowledged
     */
    public void updateOrderState(String id, State state) throws IOException {
        this.mongoAdapter.update(
                Filters.eq(ID_FIELD, new ObjectId(id)),
                new Document(STATE_FIELD, state.getId()));
    }

    /**
     * Updates the Field State to {@link State#ORDERD}
     * @param id
     * @throws IOException
     */
    public void completeOrder(String id) throws IOException {
        updateOrderState(id, State.ORDERD);
    }

    /**
     * Updates the Field State to {@link State#CANCELLED}
     * @param id
     * @throws IOException
     */
    public void cancelOrder(String id) throws IOException {
        updateOrderState(id, State.CANCELLED);
    }
}

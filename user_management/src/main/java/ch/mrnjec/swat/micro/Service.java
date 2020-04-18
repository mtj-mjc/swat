package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.bus.*;
import ch.mrnjec.swat.entities.Group;
import ch.mrnjec.swat.entities.Store;
import ch.mrnjec.swat.entities.User;
import ch.mrnjec.swat.mongo.AdapterFactory;
import ch.mrnjec.swat.mongo.GroupAdapter;
import ch.mrnjec.swat.mongo.UserAdapter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

public final class Service implements AutoCloseable {
    private static final Logger LOG = LogManager.getLogger(Service.class);
    private static final String ROUTE_USER_CREATE = "user.create";
    private static final String ROUTE_GROUP_LIST = "group.list";
    private static final String ROUTE_STORE_GET_FROM_USER = "store.from_user";
    private static final String ROUTE_STORE_LIST = "store.list";
    private final String exchangeName;
    private final BusConnector bus;

    /**
     * @throws IOException
     * @throws TimeoutException
     */
    Service() throws IOException, TimeoutException {
        this(new BusConnector());
    }

    Service(BusConnector bus) throws IOException, TimeoutException {
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = bus;
        this.bus.connect();
        this.registerRoute(ROUTE_USER_CREATE, this::onUserCreate);
        this.registerRoute(ROUTE_GROUP_LIST, this::getGroupList);
        this.registerRoute(ROUTE_STORE_GET_FROM_USER, this::getStoreFromUser);
        this.registerRoute(ROUTE_STORE_LIST, this::getStoreList);
    }

    private void onUserCreate(String message, String replyTo, String correlationId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        User user = mapper.readValue(message, User.class);
        AdapterFactory adapterFactory = new AdapterFactory();
        UserAdapter adapter = (UserAdapter) adapterFactory.getAdapter(User.class);
        try {
            adapter.create(user);
            Response response = new Response(Status.OK, "","User created");
            String jsonResponse = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, replyTo, jsonResponse, correlationId);
        } catch (IOException e) {
            Response response = new Response(Status.BAD_REQUEST, "", "Error creating user");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, replyTo, responseJson, correlationId);
        }


    }

    private void getStoreList(String message, String replyTo, String correlationId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            StoreService service = new StoreService();
            List<Store> stores = service.getAllStores();
            String storesJson = mapper.writeValueAsString(stores);
            Response response = new Response(Status.OK, storesJson, "");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, replyTo, responseJson, correlationId);
        }catch (IOException ex) {
            LOG.error(ex);
            Response response = new Response(Status.ERROR, "", "an error occured");
            String responseJson = mapper.writeValueAsString(response);
            bus.talkAsync(this.exchangeName, replyTo,responseJson, correlationId);
        }
    }

    /**
     * Replies the storeId based on the User
     * @param username User.
     * @param replyTo replyTo address.
     * @param correlationId
     * @throws IOException
     */
    private void getStoreFromUser(String username, String replyTo, String correlationId) throws IOException {
        AdapterFactory factory = new AdapterFactory();
        UserAdapter userAdapter = (UserAdapter) factory.getAdapter(User.class);
        Response response;
        try {
            User user = userAdapter.getByUsername(username);
            response = new Response(Status.OK, user.getStoreid(), "");
        } catch (NoSuchElementException ex) {
            response = new Response(Status.ERROR, "", "Error retrieving store from user");
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(response);
        bus.talkAsync(this.exchangeName, replyTo, jsonResponse, correlationId);
    }

    /**
     * @throws IOException
     */
    private void registerRoute(String route, MessageReceiver receiver) throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", route);
        bus.listenFor(exchangeName, "ServiceTemplate | " + route, route, receiver);
    }

    private void getGroupList(String message, String replyTo, String correlationId) throws IOException {
        AdapterFactory factory = new AdapterFactory();
        GroupAdapter adapter = (GroupAdapter) factory.getAdapter(Group.class);
        List<Group> groups = adapter.getAll();
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(groups);
        Response response = new Response(Status.OK, data, "");
        String result = mapper.writeValueAsString(response);

        if (result.length() > 0) {
            bus.talkAsync(this.exchangeName, replyTo, result, correlationId);
        } else {
            bus.talkAsync(this.exchangeName, replyTo, "No groups found", correlationId);
        }
    }

    @Override
    public void close() {
        throw(new UnsupportedOperationException());
    }
}

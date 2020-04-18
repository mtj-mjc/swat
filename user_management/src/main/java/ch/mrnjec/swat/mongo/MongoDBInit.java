package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Group;
import ch.mrnjec.swat.entities.Store;
import ch.mrnjec.swat.entities.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Initialize User Management Database
 *
 * @since: 14.05.2019
 * @author: Matej Mrnjec
 */
public class MongoDBInit {

    public static void main(final String[] args) throws IOException {
        deleteDatabase(MongoDbConfig.DATABASE);
        createGroups();
        createStores();
        createUsers();
    }

    private static void deleteDatabase(String name){
        MongoDbConfig config = new MongoDbConfig();
        try (MongoClient mongoClient = new MongoClient(new MongoClientURI(config.getConnectionAddress()))) {
            mongoClient.getDatabase(name).drop();
        }
    }

    public static void createUsers() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        UserAdapter adapter = (UserAdapter) factory.getAdapter(User.class);
        GroupAdapter grpAdapter = (GroupAdapter) factory.getAdapter(Group.class);
        StoreAdapter storeAdapter = (StoreAdapter) factory.getAdapter(Store.class);
        List<User> users = new ArrayList<>();
        users.add(new User(new ObjectId(),"pku", "Peter", "Kurt",
                Integer.toString(Objects.hash("123456")),
                grpAdapter.getByName("SysAdmin").getId().toString(),
                storeAdapter.getByName("Horw").getId().toString()));
        users.add(new User(new ObjectId(),"abus", "Andrew", "Bussmann",
                Integer.toString(Objects.hash("123456")),
                grpAdapter.getByName("Filialleiter").getId().toString(),
                storeAdapter.getByName("Horw").getId().toString()));
        users.add(new User(new ObjectId(),"flaz", "Flavio", "Lazzarini",
                Integer.toString(Objects.hash("123456")),
                grpAdapter.getByName("Verkäufer").getId().toString(),
                storeAdapter.getByName("Horw").getId().toString()));
        users.forEach(usr -> {
            try {
                adapter.create(usr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void createGroups() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        GroupAdapter adapter = (GroupAdapter) factory.getAdapter(Group.class);
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(new ObjectId(), "SysAdmin"));
        groups.add(new Group(new ObjectId(), "Filialleiter"));
        groups.add(new Group(new ObjectId(), "Verkäufer"));
        groups.add(new Group(new ObjectId(), "Datentypist"));
        groups.forEach(grp -> {
            try {
                adapter.create(grp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void createStores() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        StoreAdapter adapter = (StoreAdapter) factory.getAdapter(Store.class);
        List<Store> stores = new ArrayList<>();
        stores.add(new Store(new ObjectId(), "Horw"));
        stores.add(new Store(new ObjectId(), "Ebikon"));
        stores.add(new Store(new ObjectId(), "Luzern Bahnhof"));
        stores.add(new Store(new ObjectId(), "Rotkreuz"));
        stores.forEach(store -> {
            try {
                adapter.create(store);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

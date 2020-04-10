package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Entity;
import ch.mrnjec.swat.entities.User;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test Class for {@link MongoAdapter} .
 *
 * @since: 10.05.2019
 * @author: Matej Mrnjec
 */
class MongoAdapterTest {
    public static final String TEST_STR = "Test";
    public final static String NOID = "000000000000000000000000";
    public static final ObjectId TEST_ID = new ObjectId(NOID);

    /**
     * Test method for {@link MongoAdapter#setType(Class)} .
     */
    @Test
    void setType() {
        MongoClient client = mock(MongoClient.class);
        MongoAdapter<Entity> adapter = new MongoAdapter<Entity>(client);
        adapter.setType(Entity.class);
        assertTrue(adapter.getType().equals(Entity.class));
    }

    /**
     * Test method for {@link MongoAdapter#get(Document)} .
     */
    @Test
    void getTest() throws IOException, NoSuchFieldException {
        // Create Test Data

        List<User> testData = Arrays.asList(new User(TEST_ID,TEST_STR,TEST_STR, TEST_STR,TEST_STR, NOID, NOID),
                                            new User(TEST_ID,TEST_STR,TEST_STR, TEST_STR,TEST_STR, NOID, NOID),
                                            new User(TEST_ID, TEST_STR,TEST_STR, TEST_STR,TEST_STR, NOID, NOID));
        List<Document> testDoc = new ArrayList<>();

        // Mock MongoDB
        MongoClient client = mock(MongoClient.class);
        MongoCollection collection = mock(MongoCollection.class);
        MongoDatabase database = mock(MongoDatabase.class);
        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor<Document> cursor = mock(MongoCursor.class);

        // Define Return
        when(client.getDatabase(anyString())).thenReturn(database);
        when(database.getCollection(anyString())).thenReturn(collection);
        when(collection.find(any(Document.class))).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(true,true,true,false);

        // Create MongoAdapter
        MongoAdapter<User> adapter = new MongoAdapter<User>(client);
        adapter.setType(User.class);

        // Set inner Field Collection
        FieldSetter.setField(adapter, adapter.getClass().getDeclaredField("collection"),collection);

        // Prepare Testdata for compare (cast to Document)
        testData.forEach(usr -> {
            try {
                testDoc.add(adapter.convert(usr));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Return each element of Test Data
        when(cursor.next()).thenReturn(testDoc.get(0)).thenReturn(testDoc.get(1)).thenReturn(testDoc.get(2));

        assertTrue(testData.equals(adapter.get(new Document())));
    }

    /**
     * Test method for {@link MongoAdapter#getOne(Document)} .
     */
    @Test
    void getOne() throws IOException, NoSuchFieldException {
        // Create Test Data
        User user = new User(TEST_ID, TEST_STR,TEST_STR, TEST_STR,TEST_STR, NOID, NOID);
        List<Document> testDoc = new ArrayList<>();

        // Mock MongoDB
        MongoClient client = mock(MongoClient.class);
        MongoCollection collection = mock(MongoCollection.class);
        MongoDatabase database = mock(MongoDatabase.class);
        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor<Document> cursor = mock(MongoCursor.class);

        // Define Return
        when(client.getDatabase(anyString())).thenReturn(database);
        when(database.getCollection(anyString())).thenReturn(collection);
        when(collection.find(any(Document.class))).thenReturn(iterable);
        when(iterable.limit(anyInt())).thenReturn(iterable);

        // Create MongoAdapter
        MongoAdapter<User> adapter = new MongoAdapter<User>(client);
        adapter.setType(User.class);

        // Set inner Field Collection
        FieldSetter.setField(adapter, adapter.getClass().getDeclaredField("collection"),collection);

        // Return Test User
        when(iterable.first()).thenReturn(adapter.convert(user));

        assertTrue(user.equals(adapter.getOne(new Document())));
    }
}
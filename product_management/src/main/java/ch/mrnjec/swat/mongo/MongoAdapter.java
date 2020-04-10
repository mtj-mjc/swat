package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Entity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This Class is an Adapter for the MongoDB.
 *
 * @since: 17.04.2019
 * @author: Matej Mrnjec
 */
final class MongoAdapter<T extends Entity> {

    private static final Logger LOGGER = LogManager.getLogger(AdapterFactory.class);

    // Type is needed for converting
    private Class<T> type;
    // MongoDB
    private MongoDatabase database;
    private MongoCollection collection;
    private MongoClient mongoClient;

    MongoAdapter(MongoClient mongoClient){
        this.mongoClient = mongoClient;
    }

    /**
     * Gets Type with which it was initalized
     * @return Type
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Sets type
     * @param type Type
     */
    public void setType(Class<T> type){
        this.type = type;
    }

    /**
     * Changes the Collection on which the Adapter operates
     * @param database Database
     * @param collection Collection
     */
    public void changeCollection(String database, String collection){
        this.database = mongoClient.getDatabase(database);
        this.collection =  this.database.getCollection(collection);
        LOGGER.info("Changed to Database: " + database + " Collection: " + collection);
    }

    /**
     * Creates the obj on the Database.
     * @param obj Object that should be created in Collection
     * @throws IOException if convert to Document fails
     */
    public void create(T obj) throws IOException {
        Document doc = convert(obj);
        this.collection.insertOne(convert(obj));
        LOGGER.info("Created " + doc);
    }

    /**
     * Removes the obj from the Database.
     * @param obj Object that should be removed in Collection
     * @throws IOException if convert to Document fails
     */
    public Boolean remove(T obj) throws IOException {
        Document doc = convert(obj);
        Boolean result = this.collection.deleteOne(doc).wasAcknowledged();
        LOGGER.info("Removed " + doc);
        return result;
    }

    /**
     * Updates one Obj on which the filter applies and changes it to obj
     * @param filter Filter
     * @param obj New Object that will be set instead
     * @throws IOException if convert to Document fails or update is not acknowledged
     */
    public void update(Bson filter, T obj) throws IOException {
        Document doc = convert(obj);
        Bson update = new Document("$set", doc);
        if(!this.collection.updateOne(filter, update).wasAcknowledged()){
            throw new IOException(MongoDbConfig.UPDATE_NOT_ACKNOWLEDGED);
        }
        LOGGER.info("Updated all Documents which match \\nFilter: " + filter + " with Value: "+ doc);
    }

    /**
     * Updates all Obj on which the filter applies and changes it to update value.
     * @param filter Filter
     * @param update Which Fields and Values should be updated
     * @throws IOException if convert to Document fails or update is not acknowledged
     */
    public void update(Bson filter, Bson update) throws IOException{
        update = new Document("$set", update);
        if(!this.collection.updateMany(filter, update).wasAcknowledged()){
            throw new IOException(MongoDbConfig.UPDATE_NOT_ACKNOWLEDGED);
        }
    }

    /**
     * Gets all Obj that match with the query.
     * @param query query
     * @return All Obj that match the query.
     * @throws IOException if convert to Document fails
     */
    public List<T> get(Bson query) throws IOException{
        List<T> objs = new ArrayList<>();
        MongoCursor iterator =  this.collection.find(query).iterator();
        while(iterator.hasNext()){
            objs.add(convert((Document)iterator.next()));
        }
        LOGGER.info("Get all Documents which match Filter: " + query);
        return objs;
    }

    /**
     * Gets all Obj from Collection.
     * It uses the get function with query = empty Document.
     * @return All Obj in collection.
     * @throws IOException if convert to Document fails
     */
    public List<T> getAll() throws IOException {
        return get(new Document());
    }

    /**
     * Gets exactly one Obj from the Collection that matches the query.
     * @param query query
     * @return Gets one Obj that matches with the query.
     * @throws NoSuchElementException Gets thrown if no Obj is found that matches the query.
     * @throws IOException if convert to Document fails
     */
    public T getOne(Bson query) throws NoSuchElementException, IOException {
        LOGGER.info("Get one Document which match Filter: " + query);
        Document result = (Document) this.collection.find(query).limit(1).first();
        if(result != null)
            return convert(result);
        throw new NoSuchElementException("Could not find Element with query="+query);
    }

    /**
     * Converts Document to T. Document gets converted to JSON and then to T.
     * @param doc Document to parse
     * @return T
     * @throws IOException if convert fails
     */
    public T convert(Document doc) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(doc.toJson(), this.type);
    }

    /**
     * Converts T to Document.
     * @param obj Obj to parse
     * @return Document
     * @throws IOException if convert fails
     */
    public Document convert(T obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return Document.parse(mapper.writeValueAsString(obj));
    }
}

package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Product;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * This Class is an Adapter to the MongoDB for the Entity Product.
 *
 * @since: 23.04.2019
 * @author: Matej Mrnjec
 */
public final class ProductAdapter implements EntityAdapterExtended<Product> {
    public static final String ID_FIELD = "_id";
    public static final String COUNT_FIELD = "count";
    private MongoAdapter<Product> mongoAdapter;


    ProductAdapter(MongoAdapter mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
        this.mongoAdapter.changeCollection(MongoDbConfig.DATABASE, MongoDbConfig.PRODUCTS_COLLECTION);
    }

    @Override
    public MongoAdapter<Product> getMongoAdapter() {
        return mongoAdapter;
    }

    /**
     * Checks if a Product already exists. It uses the getById Method to check if Product already exists.
     * @param product Product which has to be checked
     * @return If found then true else false
     */
    public Boolean exists(Product product){
        try{
            getById(product.getId().toString());
        }
        catch (NoSuchElementException ex){
            return false;
        }
        return true;
    }

    /**
     * Subtract Count of Product by quantity and updated Filed in DB
     * @param product Product which gets booked
     * @param quantity Quantity
     * @throws IOException If not Acknowledged
     */
    public void bookProduct(Product product, int quantity) throws IOException {
        Product productInStore = getById(product.getId().toString());
        int newQuantity = productInStore.getCount() - quantity;
        this.mongoAdapter.update(
            Filters.eq(ID_FIELD, product.getId()),
            new Document(COUNT_FIELD, newQuantity));
    }
}
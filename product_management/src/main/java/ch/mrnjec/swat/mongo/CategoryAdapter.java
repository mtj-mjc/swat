package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Category;

import java.util.NoSuchElementException;

/**
 * This Class is an Adapter to the MongoDB for the Entity Category.
 *
 * @since: 23.04.2019
 * @author: Matej Mrnjec
 */
public class CategoryAdapter implements EntityAdapterExtended<Category> {
    private MongoAdapter<Category> mongoAdapter;

    CategoryAdapter(MongoAdapter mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
        this.mongoAdapter.changeCollection(MongoDbConfig.DATABASE, MongoDbConfig.CATEGORY_COLLECTION);
    }

    @Override
    public MongoAdapter<Category> getMongoAdapter() {
        return mongoAdapter;
    }

    /**
     * Checks if a Category already exists. It uses the getById Method to check if Category already exists.
     * @param category Category which has to be checked
     * @return If found then true else false
     */
    public Boolean exists(Category category){
        try{
            getByName(category.getName());
        }
        catch (NoSuchElementException ex){
            return false;
        }
        return true;
    }
}

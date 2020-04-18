package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Store;

import java.util.NoSuchElementException;

/**
 * This Class is an Adapter to the MongoDB for the Entity Store.
 *
 * @since: 24.04.2019
 * @author: Matej Mrnjec
 */
public final class StoreAdapter implements EntityAdapterExtended<Store>{

    private MongoAdapter<Store> mongoAdapter;

    StoreAdapter(MongoAdapter<Store> mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
        this.mongoAdapter.changeCollection(MongoDbConfig.DATABASE, MongoDbConfig.STORES_COLLECTION);
    }

    @Override
    public MongoAdapter<Store> getMongoAdapter() {
        return this.mongoAdapter;
    }

    @Override
    public boolean exists(Store store){
        try{
            getByName(store.getName());
        }
        catch (NoSuchElementException ex){
            return false;
        }
        return true;
    }
}

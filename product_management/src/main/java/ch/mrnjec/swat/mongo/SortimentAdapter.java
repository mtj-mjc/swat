package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Sortiment;

import java.util.NoSuchElementException;

/**
 * This Class is an Adapter to the MongoDB for the Entity Sortiment.
 *
 * @since: 23.04.2019
 * @author: Matej Mrnjec
 */
public class SortimentAdapter implements EntityAdapterExtended<Sortiment> {
    private MongoAdapter<Sortiment> mongoAdapter;

    SortimentAdapter(MongoAdapter<Sortiment> mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
        this.mongoAdapter.changeCollection(MongoDbConfig.DATABASE, MongoDbConfig.SORTIMENT_COLLECTION);
    }

    @Override
    public MongoAdapter<Sortiment> getMongoAdapter() {
        return mongoAdapter;
    }

    /**
     * Checks if a Sortiment already exists. It uses the getById Method to check if Sortiment already exists.
     * @param sortiment Sortiment which has to be checked
     * @return If found then true else false
     */
    public boolean exists(Sortiment sortiment){
        try{
            getByName(sortiment.getName());
        }
        catch (NoSuchElementException ex){
            return false;
        }
        return true;
    }
}

package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Group;

import java.util.NoSuchElementException;

/**
 * This Class is an Adapter to the MongoDB for the Entity Group.
 *
 * @since: 23.04.2019
 * @author: Matej Mrnjec
 */
public final class GroupAdapter implements EntityAdapterExtended<Group>{
    private MongoAdapter<Group> mongoAdapter;

    GroupAdapter(MongoAdapter<Group> mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
        this.mongoAdapter.changeCollection(MongoDbConfig.DATABASE, MongoDbConfig.GROUPS_COLLECTION);
    }

    @Override
    public MongoAdapter<Group> getMongoAdapter() {
        return mongoAdapter;
    }

    /**
     * Checks if a Group already exists. It uses the getById Method to check if Group already exists.
     * @param group Group which has to be checked
     * @return If found then true else false
     */
    public boolean exists(Group group){
        try{
            getByName(group.getName());
        }
        catch (NoSuchElementException ex){
            return false;
        }
        return true;
    }
}
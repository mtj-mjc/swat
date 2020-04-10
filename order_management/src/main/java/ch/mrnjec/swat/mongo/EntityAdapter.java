package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Entity;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Type EntityAdapter (needed for {@link AdapterFactory})
 * It adapts the MongoAdapter so Entity's can be used directly.
 *
 * @since: 09.05.2019
 * @author: Matej Mrnjec
 */
public interface EntityAdapter<T extends Entity> {

    /**
     * Gets every saved Entity from the Database.
     * @return List of Entitys. If no Entity is found then a empty List is returned
     */
    default List<T> getAll(){
        List<T> results = Collections.emptyList();
        try {
            results = getMongoAdapter().getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Gets every saved Entity which matches filter from the Database.
     * @param filter filter
     * @return List of found Entity's that match filter
     */
    default List<T> getBy(Bson filter){
        List<T> results = Collections.emptyList();
        try {
            results = getMongoAdapter().get(filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Gets a Entity by Filter from the Database.
     * @param filter Filter
     * @return Entity which match filter
     * @throws NoSuchElementException If no Entity is found
     */
    default T getOneBy(Bson filter) throws NoSuchElementException{
        try {
            return getMongoAdapter().getOne(filter);
        } catch (IOException e) {
            e.printStackTrace();
            throw new NoSuchElementException(e.getMessage());
        }
    }

    /**
     * Creates a Entity in the Database.
     * Note: this Method checks if a Entity with the same id already exists in the Database.
     * @param entity Entity which has to be created
     * @throws IOException if convert fails in MongoAdapter
     */
    default void create(T entity) throws IOException{
        if(!exists(entity)){
            getMongoAdapter().create(entity);
        }
    }

    /**
     * Removes this Entity form the Database.
     * Note: this Method checks if a Entity with the same id already exists in the Database.
     * @param entity Entity which has to be removed
     * @throws IOException if convert fails in MongoAdapter or update was not acknowledged
     */
    default void remove(T entity) throws IOException{
        if(exists(entity)){
            if(!getMongoAdapter().remove(entity))
                throw new IOException(MongoDbConfig.REMOVE_NOT_ACKNOWLEDGED);
        }
    }

    /**
     * Checks if a Entity already exists. It uses the getById Method to check if Entity already exists.
     * @param entity Entity which has to be checked
     * @return If found then true else false
     */
    Boolean exists(T entity);

    /**
     * Gets the MongoAdapter
     * @return MongoAdapter
     */
    MongoAdapter<T> getMongoAdapter();
}

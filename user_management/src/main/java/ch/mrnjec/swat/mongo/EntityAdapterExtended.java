package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Entity;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

import java.util.NoSuchElementException;

/**
 * Type EntityAdapter (needed for {@link AdapterFactory})
 * It adapts the MongoAdapter so Entity's can be used directly.
 * It extends the EntityAdapter and offers some default get Functions.
 *
 * @since: 14.05.2019
 * @author: Matej Mrnjec
 */
public interface EntityAdapterExtended<T extends Entity> extends EntityAdapter<T> {
    String ID_FIELD = "_id";
    String NAME_FIELD = "name";

    /**
     * Get the Field Name for ID
     * @return ID Field name
     */
    default String getIdField(){
        return ID_FIELD;
    }

    /**
     * Gets the Field Name for Name
     * @return Name Field name
     */
    default String getNameField(){
        return NAME_FIELD;
    }

    /**
     * Gets a Entity by ID from the Database.
     * Uses {@link EntityAdapterExtended#getNameField()} to determine the Field Name.
     * If Field Name differs from {@link EntityAdapterExtended#NAME_FIELD} than just override
     * {@link EntityAdapterExtended#getNameField()}
     * @param name Entity name
     * @return Entity with the name
     * @throws NoSuchElementException If no Entity is found
     */
    default T getByName(String name) throws NoSuchElementException{
        return getOneBy(Filters.eq(getNameField(), name));
    }

    /**
     * Gets a Entity by ID from the Database.
     * Uses {@link EntityAdapterExtended#getIdField()} to determine the Field Id.
     * If Field Id differs from {@link EntityAdapterExtended#ID_FIELD} than just override
     * {@link EntityAdapterExtended#getIdField()}
     * @param id Entity id
     * @return Entity with the ID
     * @throws NoSuchElementException If no Entity is found
     */
    default T getById(String id) throws NoSuchElementException{
        return getOneBy(Filters.eq(getIdField(), new ObjectId(id)));
    }
}

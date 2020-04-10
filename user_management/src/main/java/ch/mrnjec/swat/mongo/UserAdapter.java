package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.User;
import com.mongodb.client.model.Filters;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * This Class is an Adapter to the MongoDB for the Entity User.
 *
 * @since: 17.04.2019
 * @author: Matej Mrnjec
 */
public final class UserAdapter implements EntityAdapter<User>{

    // User Entity Fields which are saved in the Database
    public final static String USERNAME_FIELD = "username";

    private MongoAdapter<User> mongoAdapter;

    UserAdapter(MongoAdapter mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
        this.mongoAdapter.changeCollection(MongoDbConfig.DATABASE, MongoDbConfig.USERS_COLLECTION);
    }

    @Override
    public MongoAdapter<User> getMongoAdapter() {
        return this.mongoAdapter;
    }

    @Override
    public Boolean exists(User user){
        try{
            getByUsername(user.getUsername());
        }
        catch (NoSuchElementException ex){
            return false;
        }
        return true;
    }

    /**
     * Gets a User by Username from the Database.
     * @param username Username
     * @return User with the Username
     * @throws NoSuchElementException If no User is found
     */
    public User getByUsername(String username) throws NoSuchElementException{
        try {
            return this.mongoAdapter.getOne(Filters.eq(USERNAME_FIELD, username));
        } catch (IOException e) {
            e.printStackTrace();
            throw new NoSuchElementException(e.getMessage());
        }
    }

    /**
     * Updates User in the Database
     * @param updatedUser User with updated values
     * @throws IOException if convert fails in MongoAdapter or update was not acknowledged
     */
    public void update(User updatedUser) throws IOException {
        this.mongoAdapter.update(Filters.eq(USERNAME_FIELD, updatedUser.getUsername()), updatedUser);
    }
}

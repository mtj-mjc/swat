package ch.mrnjec.swat.entities;

import ch.mrnjec.swat.utils.ObjectIdDeserializer;
import ch.mrnjec.swat.utils.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

import java.util.Objects;

/**
 * Datamodel for User
 *
 * @since: 17.04.2019
 * @author: Matej Mrnjec
 */
public class User implements Entity{
    @JsonSerialize(using= ObjectIdSerializer.class)
    private ObjectId id;
    private String username;
    private String name;
    private String lastname;
    private String groupid;
    private String storeid;
    private String password;

    private User(){}

    /**
     * Constructor.
     * @param username Unique Username.
     * @param name name of User.
     * @param lastname lastname of User.
     * @param password password of User.
     * @param groupid id of Group
     * @param storeid id of Store
     */
    public User(ObjectId id, String username, String name, String lastname, String password, String groupid, String storeid) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.groupid = groupid;
        this.storeid = storeid;
    }

    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId getId() {
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getLastname(){
        return lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public String getGroupid(){
        return groupid;
    }

    public void setGroupid(String groupid){
        this.groupid = groupid;
    }

    public String getStoreid(){
        return storeid;
    }

    public void setStoreid(String storeid){
        this.storeid = storeid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return  id.equals(user.id) &&
                username.equals(user.username) &&
                name.equals(user.name) &&
                lastname.equals(user.lastname) &&
                password.equals(user.password) &&
                groupid.equals(user.groupid) &&
                storeid.equals(user.storeid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, name, lastname, password, groupid, storeid);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", groupid='" + groupid + '\'' +
                ", storeid='" + storeid + '\'' +
                '}';
    }
}

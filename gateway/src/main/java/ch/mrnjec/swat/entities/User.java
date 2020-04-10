package ch.mrnjec.swat.entities;

import java.util.Objects;

/**
 * Datamodel for User
 *
 * @since: 17.04.2019
 * @author: matej
 */
public class User {
    private String username;
    private String name;
    private String lastname;
    private String groupid;
    private String storeid;

    /**
     * Constructor.
     * @param username Unique Username.
     * @param name name of User.
     * @param lastname lastname of User.
     * @param groupid id of Group
     * @param storeid id of Store
     */
    public User(String username, String name, String lastname, String groupid, String storeid) {
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.groupid = groupid;
        this.storeid = storeid;
    }

    public User() {
        this.username = "";
        this.name = "";
        this.lastname = "";
        this.groupid = Group.NOID;
        this.storeid = Group.NOID;
    }

    /**
     * Default Constructor.
     */
    public User(String username, String name, String lastname) {
        this(username, name, lastname, Group.NOID, Store.NOID);
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

    public String getGroupId(){
        return groupid;
    }

    public void setGroupId(String groupid){
        this.groupid = groupid;
    }

    public String getStoreId(){
        return storeid;
    }

    public void setStoreId(String storeid){
        this.storeid = storeid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return  username.equals(user.username) &&
                name.equals(user.name) &&
                lastname.equals(user.lastname) &&
                groupid.equals(user.groupid) &&
                storeid.equals(user.storeid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, lastname, groupid, storeid);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", groupid='" + groupid + '\'' +
                ", storeid='" + storeid + '\'' +
                '}';
    }
}

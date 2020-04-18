package ch.mrnjec.swat.entities;

import java.util.Objects;

/**
 * Datamodel for a Store
 *
 * @since: 23.04.2019
 * @author: matej
 */
public class Store {

    public static final String NOID = "-1";
    private String id;
    private String name;

    /**
     * Constructor
     * @param id Unique id
     * @param name Store Name
     */
    public Store(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Store(String name) {
        this(NOID, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id.equals(store.id) &&
                name.equals(store.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

package ch.mrnjec.swat.entities;

import java.util.Objects;

/**
 * Datamodel for Group
 *
 * @since: 23.04.2019
 * @author: matej
 */
public class Group {
    public final static String NOID = "-1";
    private String id;
    private String name;

    /**
     * Constructor
     * @param id Unigue Group id
     * @param name Group Name
     */
    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group(String name) {
        this(NOID, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id.equals(group.id) &&
                name.equals(group.name);
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
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

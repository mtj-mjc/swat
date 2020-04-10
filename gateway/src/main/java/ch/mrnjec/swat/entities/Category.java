package ch.mrnjec.swat.entities;

public class Category {
    private static final int NOID = -1;

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Category() {
        this(NOID);
    }

    public Category(int id) {
        this.id = id;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }


}

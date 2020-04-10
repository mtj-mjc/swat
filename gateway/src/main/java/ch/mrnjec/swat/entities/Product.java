package ch.mrnjec.swat.entities;

import java.math.BigDecimal;

public class Product {
    private static final int NOID = -1;

    private int id;
    private String name;
    private BigDecimal price;
    private String description;
    private Category category;

    public Product() {
        this(NOID);
    }

    public Product(int id) {
        this.id = id;
    }
    public Product(int id, String name, BigDecimal price, String description, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}

package ch.mrnjec.swat.entities;

import ch.mrnjec.swat.utils.ObjectIdDeserializer;
import ch.mrnjec.swat.utils.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

import java.util.Objects;

/**
 * Datamodel for Product
 *
 * @since: 14.05.2019
 * @author: Matej Mrnjec
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Entity {
    @JsonSerialize(using= ObjectIdSerializer.class)
    private ObjectId id;
    private String name;
    private String description;
    private double price;
    private String sortimentid;
    private String categoryid;

    private Product(){}

    /**
     * Default Constructor
     * @param id ObjectId
     * @param name Name
     * @param description Description
     * @param price Price
     * @param sortimentid
     * @param categoryid
     */
    public Product(ObjectId id, String name, String description, double price, String sortimentid, String categoryid) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sortimentid = sortimentid;
        this.categoryid = categoryid;
    }

    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSortimentid() {
        return sortimentid;
    }

    public void setSortimentid(String sortimentid) {
        this.sortimentid = sortimentid;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                id.equals(product.id) &&
                name.equals(product.name) &&
                description.equals(product.description) &&
                sortimentid.equals(product.sortimentid) &&
                categoryid.equals(product.categoryid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, sortimentid, categoryid);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", sortimentid='" + sortimentid + '\'' +
                ", categoryid='" + categoryid + '\'' +
                '}';
    }
}

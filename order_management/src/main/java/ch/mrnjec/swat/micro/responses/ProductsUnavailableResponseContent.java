package ch.mrnjec.swat.micro.responses;

import ch.mrnjec.swat.entities.Product;
import ch.mrnjec.swat.utils.ObjectIdDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ProductsUnavailableResponseContent {

    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId orderid;
    private List<Product> unavailableProducts;
    private List<Product> nonExistingProducts;

    public List<Product> getUnavailableProducts() {
        return unavailableProducts;
    }

    private ProductsUnavailableResponseContent() {

    }
    public void setUnavailableProducts(List<Product> unavailableProducts) {
        this.unavailableProducts = unavailableProducts;
    }

    public List<Product> getNonExistingProducts() {
        return nonExistingProducts;
    }

    public void setNonExistingProducts(List<Product> nonExistingProducts) {
        this.nonExistingProducts = nonExistingProducts;
    }


    public ProductsUnavailableResponseContent(ObjectId orderId) {
        this.orderid = orderId;
        unavailableProducts = new ArrayList<>();
        nonExistingProducts = new ArrayList<>();
    }

    public void addAvailableProduct(Product product) {
        this.unavailableProducts.add(product);
    }

    public void addNonExistingProduct(Product product) {
        this.nonExistingProducts.add(product);
    }

    public void removeAvailableProduct(Product product) {
        this.unavailableProducts.remove(product);
    }

    public void removeNonExistingProduct(Product product) {
        this.nonExistingProducts.remove(product);
    }

    public ObjectId getOrderId() {
        return orderid;
    }
}

package ch.mrnjec.swat.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private static final int NOID = -1;

    private int id;
    /**
     * List of related Articles (just the IDs)
     */
    private List<Product> articles;
    private String userId;
    private String customerId;
    private String time;

    public Order() {

        this(NOID);
    }

    public Order(int id) {
        this.id  = id;
        this.articles = new ArrayList();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final int id) {
        this.id = id;
    }


    public List<Product> getProduct() {
        return this.articles;
    }

    public void addProduct(Product product){
        this.articles.add(product);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

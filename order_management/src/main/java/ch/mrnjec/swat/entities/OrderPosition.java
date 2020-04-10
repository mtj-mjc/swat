package ch.mrnjec.swat.entities;

import java.util.Objects;

/**
 * Position in a Order
 *
 * @since: 17.05.2019
 * @author: matej
 */
public class OrderPosition {
    private Product product;
    private int count;

    private OrderPosition(){}

    public OrderPosition(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPosition that = (OrderPosition) o;
        return count == that.count &&
                product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, count);
    }
}

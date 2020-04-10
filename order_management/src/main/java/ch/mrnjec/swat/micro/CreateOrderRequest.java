package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.entities.Order;
import org.bson.types.ObjectId;

import java.util.Objects;

public class CreateOrderRequest {
    private String correlationId;
    private String replyTo;
    private Order order;


    public CreateOrderRequest(String correlationId, String replyTo, Order order) {
        this.correlationId = correlationId;
        this.replyTo = replyTo;
        this.order = order;
    }


    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ObjectId getOrderId() {
        return order.getId();
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateOrderRequest that = (CreateOrderRequest) o;
        return Objects.equals(correlationId, that.correlationId) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correlationId, order);
    }

}

package ch.mrnjec.swat.entities;

import ch.mrnjec.swat.utils.IsoDateDeserializer;
import ch.mrnjec.swat.utils.IsoDateSerializer;
import ch.mrnjec.swat.utils.ObjectIdDeserializer;
import ch.mrnjec.swat.utils.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order implements Entity{
    @JsonSerialize(using= ObjectIdSerializer.class)
    private ObjectId id;
    private List<OrderPosition> positions;
    private String userName;
    private String customerid;
    private String storeid;
    @JsonSerialize(using= IsoDateSerializer.class)
    private Date date;
    private State state;

    private Order(){}

    public Order(ObjectId id, List<OrderPosition> positions, String userName, String customerid, String storeid, Date date, State state) {
        this.id = id;
        this.positions = positions;
        this.userName = userName;
        this.customerid = customerid;
        this.storeid = storeid;
        this.date = date;
        this.state = state;
    }

    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public List<OrderPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<OrderPosition> positions) {
        this.positions = positions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    @JsonDeserialize(using= IsoDateDeserializer.class)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", positions=" + positions +
                ", userName='" + userName + '\'' +
                ", customerid='" + customerid + '\'' +
                ", storeid='" + storeid + '\'' +
                ", date=" + date +
                ", state=" + state +
                '}';
    }
}

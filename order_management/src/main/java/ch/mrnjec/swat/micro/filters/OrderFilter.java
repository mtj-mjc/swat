package ch.mrnjec.swat.micro.filters;

import java.util.Objects;

public class OrderFilter {
    private OrderFilterField field;
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderFilter filter = (OrderFilter) o;
        return field == filter.field &&
                Objects.equals(content, filter.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, content);
    }

    public OrderFilter(OrderFilterField field, String content) {
        this.field = field;
        this.content = content;
    }

    private OrderFilter() {

    }

    public OrderFilterField getField() {
        return field;
    }

    public void setField(OrderFilterField field) {
        this.field = field;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

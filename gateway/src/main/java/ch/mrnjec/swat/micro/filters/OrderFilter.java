package ch.mrnjec.swat.micro.filters;

public class OrderFilter implements Filter{
    private OrderFilterField field;
    private String content;

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

package ch.mrnjec.swat.micro.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderFilterField {
    USERNAME(0),
    STATUS(1),
    NO_FILTER(2),
    NOT_AVAILABLE(3),
    COMPLETED_BY_USER(4);

    private int id;

    OrderFilterField(int id){
        this.id = id;
    }

    @JsonValue
    public int getId(){
        return this.id;
    }

    @JsonCreator
    public static OrderFilterField forValue(String id){
        for(OrderFilterField state : OrderFilterField.values()){
            if(Integer.toString(state.id).equals(id))
                return state;
        }
        return NOT_AVAILABLE;
    }
}
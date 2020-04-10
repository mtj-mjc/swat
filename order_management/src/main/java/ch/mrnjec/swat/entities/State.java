package ch.mrnjec.swat.entities;

import com.fasterxml.jackson.annotation.*;

/**
 * State of Order
 *
 * @since: 20.05.2019
 * @author: Matej Mrnjec
 */
public enum State {
    ORDERD(0),
    PENDING(1),
    CANCELLED(2),
    NOT_AVAILABLE(3);

    private int id;

    State(int id){
        this.id = id;
    }

    @JsonValue
    public int getId(){
        return this.id;
    }

    @JsonCreator
    public static State forValue(String id){
        for(State state : State.values()){
            if(Integer.toString(state.id).equals(id))
                return state;
        }
        return NOT_AVAILABLE;
    }
}

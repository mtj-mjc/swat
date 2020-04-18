package ch.mrnjec.swat.micro;

/**
 * "Description here"
 *
 * @since: 18.04.2020
 * @author: matej
 */
public enum ErrorMessage {
    GENERIC_ERROR("Ein Fehler ist aufgetreten"),
    INTERNAL_SERVER_ERROR("Internal Server error");

    private String message;

    ErrorMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}

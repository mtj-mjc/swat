package ch.mrnjec.swat.bus;

public class Response {
    /**
     * Status of the Response
     */
    private Status status;
    /**
     * Contains Entity JSON if Status OK
     */
    private String data;
    private String reason;

    private Response(){

    }

    public Response(Status status, String data, String reason) {
        this.status = status;
        this.data = data;
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
package models;

/**
 * Created by dell co on 3/31/2018.
 */

public class Request {
    public static int SENDER_REQUEST = 1;
    public static int RECEIVER_REQUEST = 2;
    public static int NO_REQUEST = 3;
    private int requestState = NO_REQUEST;
    private String fromDate;

    public Request() {
    }

    public Request(int requestState, String fromDate) {
        this.requestState = requestState;
        this.fromDate = fromDate;
    }

    public int getRequestState() {
        return requestState;
    }

    public void setRequestState(int requestState) {
        this.requestState = requestState;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
}

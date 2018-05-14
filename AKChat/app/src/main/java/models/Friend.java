package models;

/**
 * Created by dell co on 3/26/2018.
 */

public class Friend {

    public static int IS_FRIEND = 1;
    public static int NOT_FRIEND = 2;
    private String friendId;
    private String fromDate;

    public Friend() {

    }

    public Friend(String friendId, String fromDate) {
        this.friendId = friendId;
        this.fromDate = fromDate;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
}

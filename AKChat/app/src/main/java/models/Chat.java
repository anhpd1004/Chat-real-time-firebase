package models;

/**
 * Created by dell co on 5/2/2018.
 */

public class Chat {
    private long timestamp = 0;
    private boolean seen = false;

    public Chat() {

    }

    public Chat(long timestamp, boolean seen) {
        this.timestamp = timestamp;
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}

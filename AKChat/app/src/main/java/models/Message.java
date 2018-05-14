package models;

import com.google.firebase.database.ServerValue;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by dell co on 3/26/2018.
 */

public class Message {

    public static String[] MESSAGE_TYPE = {"text", "image", "voice", "video", "sticker"};
    public static int MESSAGE_TYPE_TEXT = 0;
    public static int MESSAGE_TYPE_IMAGE = 1;
    public static int MESSAGE_TYPE_VOICE = 2;
    public static int MESSAGE_TYPE_VIDEO = 3;
    public static int MESSAGE_TYPE_STICKER = 4;

    private Object content;//nội dung tin nhắn
    private long timestamp;//thời gian cua message
    private String type;//loại thư: ảnh, hay text, hay đoạn ghi âm, ...
    private boolean seen;//da xem hay chua
    private String from;
    private String to;

    public Message() {

    }

    public Message(boolean bool) {
        this.content = "text";
        this.timestamp = 1;
        this.type = "text";
        this.seen = false;
        this.from = "text";
        this.to = "text";
    }

    public Message(Object content, long timestamp, String type, boolean seen, String from, String to) {
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
        this.seen = seen;
        this.from = from;
        this.to = to;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
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

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setType(String type) {
        this.type = type;
    }
}

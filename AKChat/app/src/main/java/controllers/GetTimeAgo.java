package controllers;

import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;

import java.util.Random;

/**
 * Created by dell co on 5/2/2018.
 */

public class GetTimeAgo {

    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time, Context context) {
        if(time < 1000000000000L) {
            //neu timestamp tính bằng second thì chuyển sang millis second
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        long distance = now - time;
        if(distance <= 0) {
            return null;
        }
        if(distance < SECOND_MILLIS) {
            return "just now";
        }
        if(distance < MINUTE_MILLIS) {
            return (distance/SECOND_MILLIS) + " s ago";
        }
        if(distance < HOUR_MILLIS) {
            return (distance / MINUTE_MILLIS) + " m ago";
        }
        if(distance < DAY_MILLIS) {
            return (distance / HOUR_MILLIS) + " h ago";
        }
        return (distance / DAY_MILLIS) + " d ago";
    }

}

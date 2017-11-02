package com.jaf.tools.date;

import org.joda.time.Duration;

import java.util.Date;

/**
 * Created by hsk on 16/9/6.
 */
public class DurationTimeUtil {

    public static long getRemainderTime(Date onTime, long currentTime,int lastMinutes) {
        long finishTime = onTime.getTime() + lastMinutes * 60 * 1000;
        long remainderTime = 0L;
        if (finishTime > currentTime) {
            remainderTime = finishTime - currentTime;
        }
        return remainderTime;
    }

    public static String getDurationDesc(long remainderTime){
        Duration duration = Duration.millis(remainderTime);
        StringBuffer buffer = new StringBuffer()
                .append(duration.getStandardHours())
                .append(":")
                .append(duration.getStandardMinutes() - 60 * duration.getStandardHours());
        return buffer.toString();
    }
}

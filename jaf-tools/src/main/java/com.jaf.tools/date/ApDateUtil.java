package com.jaf.tools.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hsk on 2016/11/10.
 */
public class ApDateUtil {

    static Map<Integer, String> DATE_MAP = new HashMap<>();

    static {
        DATE_MAP.put(-2, "前天");
        DATE_MAP.put(-1, "昨天");
        DATE_MAP.put(0, "今天");
        DATE_MAP.put(1, "明天");
        DATE_MAP.put(2, "后天");
        DATE_MAP.put(3, "大后天");
    }

    public static String long2String(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));
    }

    public static int getDayInt(String time, String format) {
        try {
            return getDayInt(new SimpleDateFormat(format).parse(time));
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式错误:" + time);
        }
    }

    public static int getDayInt(Date date) {
        Period period = Period.between(
                LocalDate.now(),
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        );
        if (period.getYears() == 0 && period.getMonths() == 0)
            return period.getDays();
        return 1000;
    }


    public static String getDayString(Date date, String format) {
        int between = getDayInt(date);
        if (DATE_MAP.containsKey(between)) {
            return DATE_MAP.get(between);
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static String date2String(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }


    public static int calcIsThisWeek(Date date) {
        Date currentDate = new Date();
        Date current = getFirstDayOfWeek(currentDate);
        Date calc = getFirstDayOfWeek(date);
        if (current.before(calc)) {
            return -1;
        }
        return 0;
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        int first = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, first);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date getNextOneDayAt(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public static Date getNextTowDayEndAt(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        return calendar.getTime();
    }

    public static Date getTodayEndAt(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date getTodayEndAt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static Long calcLastTime(Date endTime) {
        Long lastTime = endTime.getTime() - new Date().getTime();
        return lastTime > 0 ? lastTime : 0;
    }

}

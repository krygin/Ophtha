package ru.krygin.ophtha;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by krygin on 06.09.17.
 */

public class DateTimeUtils {
    private DateTimeUtils() {

    }

    public static Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    public static String getDateString(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

    public static Date getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTime();
    }
}

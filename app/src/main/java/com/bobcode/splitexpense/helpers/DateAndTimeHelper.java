package com.bobcode.splitexpense.helpers;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by vijayananjalees on 4/13/15.
 */
public final class DateAndTimeHelper {

    public static String[] dayOfWeek = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"};

    public static String[] month = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    public static String getMobileTime() {
        Calendar calendar = Calendar.getInstance();

        String meridiem = "" + calendar.get(Calendar.AM_PM);
        if (meridiem.equals("1"))
            meridiem = "PM";
        else
            meridiem = "AM";

        String hour = "" + calendar.get(Calendar.HOUR);
        String minute = "" + calendar.get(Calendar.MINUTE);
        if (minute.length() == 1)
            minute = "0" + minute;

        return (hour + ":" + minute + meridiem);
    }

    public static String getMobileDate() {
        Calendar calendar = Calendar.getInstance();

        String dayOfTheWeek = DateAndTimeHelper.dayOfWeek[(int) calendar.get(Calendar.DAY_OF_WEEK) - 1];

        String month = "" + DateAndTimeHelper.month[calendar.get(Calendar.MONTH)];

        String date = "" + calendar.get(Calendar.DATE);

        String year = "" + calendar.get(Calendar.YEAR);

        return (dayOfTheWeek + " " + month + " " + date + " " + year);
    }

    public static String getFormattedCurrentDate() {
        Date currentSystemDate = Calendar.getInstance().getTime();
        String formatString = "EEEE, MMMM dd, yyyy";

        return DateFormat.format(formatString, currentSystemDate) + "";
    }

    public static String getRawCurrentDate() {
        Date currentSystemDate = Calendar.getInstance().getTime();
        String formatString = "MMMM dd yyyy";

        return DateFormat.format(formatString, currentSystemDate) + "";
    }

    public static String getCurrentTime() {
        Date currentSystemDate = Calendar.getInstance().getTime();
        String formatString = "h:mmaa";

        return DateFormat.format(formatString, currentSystemDate) + "";
    }
}

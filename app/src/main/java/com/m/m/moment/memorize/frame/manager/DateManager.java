package com.m.m.moment.memorize.frame.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateManager {


    public static String getDateText(int year, int month, int dayOfMonth) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        Date date = new Date();
        date.setTime(cal.getTimeInMillis());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

        return simpleDateFormat.format(date);
    }

    public static String getDateText(long dateMil) {

        Date date = new Date();
        date.setTime(dateMil);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

        return simpleDateFormat.format(date);
    }

    public static String getDateMoreText(long dateMil) {

        Date date = new Date();
        date.setTime(dateMil);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd  a hh:mm:ss");

        return simpleDateFormat.format(date);
    }
}

package com.titaniumproductionco.db.generator;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class DataGenerator {

    private Random rand;
    private int startYear;
    private int startMonth;
    private int startDay;
    private Calendar calendar;

    public DataGenerator(int startYear) {
        this.startYear = startYear;
        rand = new Random();
        init();
    }

    private void init() {
        startMonth = rand.nextInt(12);
        startDay = rand.nextInt(10) + 1;
        calendar = new GregorianCalendar(startYear, startMonth, startDay);
    }

    public Random rand() {
        return rand;
    }

    public String randomDateString(long maxDelayYear) {
        Calendar c = randomDate(maxDelayYear);
        return dateToString(c);
    }

    public Calendar randomDate(long maxDelayYear) {
        Calendar c = (Calendar) calendar.clone();
        long r = Math.abs(rand.nextLong()) % (31556952000L * maxDelayYear);
        c.setTimeInMillis(c.getTimeInMillis() + r);
        return c;
    }

    public static String dateToString(Calendar c) {
        if (c == null)
            return null;
        int y = c.get(Calendar.YEAR);
        int m = (c.get(Calendar.MONTH) + 1);
        int d = c.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", y, m, d);
    }

    public static String dateToTimeStamp(Calendar c) {
        if (c == null)
            return null;
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);
        int ms = c.get(Calendar.MILLISECOND);
        return dateToString(c) + " " + String.format("%02d:%02d:%02d.%03d", h, m, s, ms);
    }

}

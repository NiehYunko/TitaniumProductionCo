package com.titaniumproductionco.db.generator;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;

public class TimeSequence {
    private HashSet<Integer> set;
    private Calendar day;

    public TimeSequence(Calendar day) {
        this.day = (Calendar) day.clone();
        this.day.set(Calendar.HOUR_OF_DAY, 0);
        this.day.set(Calendar.MINUTE, 0);
        this.day.set(Calendar.SECOND, 0);
        this.day.set(Calendar.MILLISECOND, 0);
        set = new HashSet<>();
    }

    public String getTimeStamp(DataGenerator gen) {
        Random r = gen.rand();
        int time = r.nextInt(86400000);
        while (set.contains(time)) {
            time = r.nextInt(86400000);
        }
        set.add(time);
        Calendar c = (Calendar) day.clone();
        c.setTimeInMillis(day.getTimeInMillis() + time);
        if (Calendar.getInstance().before(c))
            return null;
        return DataGenerator.dateToTimeStamp(c);
    }
}

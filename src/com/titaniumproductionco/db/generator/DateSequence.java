package com.titaniumproductionco.db.generator;

import java.util.Calendar;

public class DateSequence {
    private Calendar current;

    public DateSequence(Calendar today) {
        current = today;
    }

    public String nextDay() {
        return DataGenerator.dateToString(nextDayDate());
    }

    public Calendar nextDayDate() {
        current.setTimeInMillis(current.getTimeInMillis() + 86400000);
        if (Calendar.getInstance().before(current))
            return null;
        return current;
    }
}

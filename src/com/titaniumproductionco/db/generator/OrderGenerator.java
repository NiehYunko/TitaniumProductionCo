package com.titaniumproductionco.db.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class OrderGenerator {
    private DateSequence dates;
    private ArrayList<ShippedToGenerator> shipGen = new ArrayList<>();

    public String[][] generateOnce(DataGenerator gen) {
        if (dates == null) {
            Calendar d = gen.randomDate(1);
            if (Calendar.getInstance().before(d)) {
                d.setTimeInMillis(System.currentTimeMillis() - 86400000L * 30L);
            }
            dates = new DateSequence(d);
        }
        Calendar date = dates.nextDayDate();
        if (date != null) {
            Random r = gen.rand();
            int orders = r.nextInt(3);
            String[][] str = new String[orders][];
            for (int i = 0; i < orders; i++) {
                int quantity = Math.abs(r.nextInt(1000) + r.nextInt(250) - r.nextInt(20)) + r.nextInt(20) + 20;
                String address = Dictionary.randomStreetNumber(r) + " " + Dictionary.randomRoad(r) + ", " + Dictionary.randomCity(r) + ", " + Dictionary.randomState(r) + ", "
                        + Dictionary.randomCountry(r);
                shipGen.add(new ShippedToGenerator(nextID, date));
                str[i] = new String[] { String.valueOf(nextID++), DataGenerator.dateToString(date), address, quantity + "" };

            }
            return str;
        }
        return null;
    }

    private int nextID = 1;

    public List<ShippedToGenerator> getShippedToGenerators() {
        return shipGen;
    }
}

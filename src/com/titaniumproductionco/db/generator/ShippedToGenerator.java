package com.titaniumproductionco.db.generator;

import java.util.Calendar;
import java.util.Random;

public class ShippedToGenerator {
    private int orderID;
    private Calendar placedDate;

    public ShippedToGenerator(int orderID, Calendar placedDate) {
        this.orderID = orderID;
        this.placedDate = (Calendar) placedDate.clone();
    }

    public String[] generate(DataGenerator gen, FactoryGenerator fGen) {
        Random r = gen.rand();
        long ship = r.nextInt(604800000);
        Calendar shipDate = (Calendar) placedDate.clone();
        shipDate.setTimeInMillis(placedDate.getTimeInMillis() + ship);
        if (shipDate.after(Calendar.getInstance()))
            return null;
        long receive = Math.abs(r.nextLong()) % (7776000000L);
        Calendar receiveDate = (Calendar) placedDate.clone();
        receiveDate.setTimeInMillis(shipDate.getTimeInMillis() + receive);
        if (receiveDate.after(Calendar.getInstance()))
            receiveDate = null;
        int factory = fGen.getRandomFactoryID(gen);
        return new String[] { factory + "", orderID + "", DataGenerator.dateToString(shipDate), DataGenerator.dateToString(receiveDate) };
    }
}

package com.titaniumproductionco.db.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MiningLogGenerator {
    private int mineID;
    private DateSequence dates;

    private int standard = -1;
    private int random = -1;
    private double material1Weight = -1;

    private ArrayList<ShipOresToGenerator> shipOresToGen;

    public MiningLogGenerator(int mineID, Calendar startDate) {
        this.mineID = mineID;
        dates = new DateSequence(startDate);
        shipOresToGen = new ArrayList<>();
    }

    public List<ShipOresToGenerator> getShipOresToGenerators() {
        return shipOresToGen;
    }

    public String[][] generateOnce(DataGenerator gen) {
        Random r = gen.rand();
        if (standard == -1) {
            standard = r.nextInt(20000) + 5000;
            random = r.nextInt(2000);
            material1Weight = r.nextDouble() * 0.5 + 0.25;
        }
        String date = dates.nextDay();
        if (date != null) {
            int mined = standard + r.nextInt(random * 2) - random;
            double m1w = material1Weight + (r.nextDouble() * 0.2 - 0.1);
            int mined1 = Math.abs((int) (mined * m1w));
            int mined2 = Math.abs(mined - mined1);
            shipOresToGen.add(new ShipOresToGenerator(date, 1, mined1, mineID));
            shipOresToGen.add(new ShipOresToGenerator(date, 2, mined2, mineID));
            return new String[][] { { date, "" + mineID, "1", "" + mined1 }, { date, "" + mineID, "2", "" + mined2 } };
        }
        return null;
    }
}

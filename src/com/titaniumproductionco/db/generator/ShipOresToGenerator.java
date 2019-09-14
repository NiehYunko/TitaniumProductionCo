package com.titaniumproductionco.db.generator;

import java.util.Random;

public class ShipOresToGenerator {
    private String date;
    private int total;
    private int mineID;
    private int matID;

    public ShipOresToGenerator(String date, int material, int qty, int mine) {
        this.date = date;
        matID = material;
        total = qty;
        mineID = mine;
    }

    public String[] generate(DataGenerator gen, FactoryGenerator fGen) {
        Random r = gen.rand();
        int fluc = total / 20 + 10;
        int amt = total + r.nextInt(fluc) - r.nextInt(fluc);
        int fact = fGen.getRandomFactoryID(gen);
        return new String[] { date, "" + mineID, "" + fact, "" + amt, "" + matID };

    }
}

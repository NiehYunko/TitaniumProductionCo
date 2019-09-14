package com.titaniumproductionco.db.generator;

public class UsedInGenerator {
    private int matID;
    private int procExecID;
    private int qty;

    public UsedInGenerator(int m, int p, int q) {
        matID = m;
        procExecID = p;
        qty = q;
    }

    public String[] generate() {
        return new String[] { matID + "", procExecID + "", qty + "" };
    }
}

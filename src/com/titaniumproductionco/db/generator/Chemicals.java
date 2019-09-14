package com.titaniumproductionco.db.generator;

public class Chemicals {
    public static final int TiO2 = 1;
    public static final int FeTiO3 = 2;
    public static final int Ti = 3;
    public static final int C = 4;
    public static final int Fe = 5;
    public static final int Cl2 = 6;
    public static final int TiCl4 = 7;
    public static final int Mg = 8;
    public static final int MgCl2 = 9;

    public static final Equation ilmenite = new Equation(1, new int[] { 1, FeTiO3, 1, C }, new int[] { 1, TiO2, 1, Fe });
    public static final Equation oxygen = new Equation(2, new int[] { 1, TiO2, 2, C, 2, Cl2 }, new int[] { 1, TiCl4 });
    public static final Equation complete = new Equation(3, new int[] { 1, TiCl4, 2, Mg }, new int[] { 1, Ti, 2, MgCl2 });

    public static final Equation[] EQUATIONS = { ilmenite, oxygen, complete };

    public static class Equation {
        int id;
        int[] inputs;
        int[] outputs;

        private Equation(int i, int[] in, int[] out) {
            id = i;
            inputs = in;
            outputs = out;
        }
    }
}

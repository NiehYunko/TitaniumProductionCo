package com.titaniumproductionco.db.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.titaniumproductionco.db.services.Role;

public class MachineGenerator {
    private int factID;
    private int machines;

    private ArrayList<ProcExecGenerator> peGen = new ArrayList<>();
    private ArrayList<UserGenerator> opGen = new ArrayList<>();

    public MachineGenerator(int f, int m) {
        factID = f;
        machines = m;
    }

    public String[][] generate(DataGenerator gen) {
        String[][] str = new String[machines][];
        int process = 0;
        for (int i = 0; i < machines; i++) {
            Random r = gen.rand();
            Calendar startDate = gen.randomDate(1);
            if (Calendar.getInstance().before(startDate)) {
                startDate.setTimeInMillis(System.currentTimeMillis());
            }
            double cycles = r.nextDouble() * 10 + 5;
            process = (process + r.nextInt(2) + 1) % 3;

            peGen.add(new ProcExecGenerator(nextID, machines, cycles, process, startDate));
            opGen.add(new UserGenerator(Role.MACHINE_OPERATOR, nextID));
            str[i] = new String[] { String.valueOf(nextID++), String.valueOf(cycles), DataGenerator.dateToString(startDate), "" + (process + 1), "" + factID };
        }
        return str;
    }

    public List<ProcExecGenerator> getProcExecGenerators() {
        return peGen;
    }

    public List<UserGenerator> getOpGenerators() {
        return opGen;
    }

    public int nextID = 1;
}

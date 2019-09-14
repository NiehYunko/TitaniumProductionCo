package com.titaniumproductionco.db.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.titaniumproductionco.db.generator.Chemicals.Equation;

public class ProcExecGenerator {
    private int machineID;
    private Equation equation;
    private double cycles;

    private DateSequence dates;

    private double eff = -1;
    private int standard = -1;
    private int random = -1;

    private int machine;

    private ArrayList<UsedInGenerator> useGen = new ArrayList<>();

    public ProcExecGenerator(int machineID, int numMachinesInFactory, double cycl, int process, Calendar startDay) {
        this.machineID = machineID;
        machine = numMachinesInFactory;
        cycles = cycl;
        equation = Chemicals.EQUATIONS[process];
        dates = new DateSequence(startDay);
    }

    public List<String[]> generateOnce(DataGenerator gen) {
        Random r = gen.rand();
        if (standard == -1) {
            standard = (int) ((r.nextInt(20000) + 5000) / cycles / (machine * (r.nextDouble() * 1.5 + 2)));
            random = Math.abs((int) (r.nextInt(2000) / cycles / (machine) * (r.nextDouble() * 1.5 + 2))) + 1;
            eff = r.nextDouble() < 0.2 ? (r.nextDouble() * 0.3 + 0.7) : (r.nextDouble() * 0.15 + 0.85);
        }

        Calendar day = dates.nextDayDate();
        if (day != null) {
            ArrayList<String[]> list = new ArrayList<>();

            TimeSequence times = new TimeSequence(day);
            int cyc = (int) (cycles + r.nextInt(7) - r.nextInt(7));
            for (int i = 0; i < cyc; i++) {
                String time = times.getTimeStamp(gen);
                if (time != null) {
                    int baseqty = Math.abs(standard + r.nextInt(random * 2) - random) + 10;
                    for (int in = 0; in < equation.inputs.length; in += 2) {
                        UsedInGenerator uGen = new UsedInGenerator(equation.inputs[in + 1], nextID, baseqty * equation.inputs[in]);
                        useGen.add(uGen);
                    }

                    baseqty = (int) (baseqty * (r.nextDouble() * 0.2 + 0.9) * eff);

                    for (int out = 0; out < equation.outputs.length; out += 2) {
                        UsedInGenerator uGen = new UsedInGenerator(equation.outputs[out + 1], nextID, baseqty * equation.outputs[out]);
                        useGen.add(uGen);
                    }

                    list.add(new String[] { String.valueOf(nextID++), machineID + "", time });
                }
            }
            return list;
        }
        return null;
    }

    public int nextID = 1;

    public List<UsedInGenerator> getUsedInGenerators() {
        return useGen;
    }
}

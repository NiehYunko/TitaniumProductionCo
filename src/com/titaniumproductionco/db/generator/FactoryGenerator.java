package com.titaniumproductionco.db.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.titaniumproductionco.db.services.Role;

public class FactoryGenerator {

    private ArrayList<UserGenerator> supervisorGenerator = new ArrayList<>();

    public String[] generate(DataGenerator gen) {
        Random rand = gen.rand();
        String address = Dictionary.randomStreetNumber(rand) + " " + Dictionary.randomRoad(rand);
        lastMGen = new MachineGenerator(nextID, rand.nextInt(4) + 2);
        supervisorGenerator.add(new UserGenerator(Role.FACTORY_SUPERVISOR, nextID));
        supervisorGenerator.add(new UserGenerator(Role.FACTORY_SUPERVISOR, nextID));

        return new String[] { String.valueOf(nextID++), address };
    }

    private MachineGenerator lastMGen;

    public MachineGenerator getLastGeneratedMachineGenerator() {
        return lastMGen;
    }

    private int nextID = 1;

    public int getRandomFactoryID(DataGenerator gen) {
        if (nextID == 2)
            return 1;
        return gen.rand().nextInt(nextID - 1) + 1;
    }

    public List<UserGenerator> getSupervisorGenerators() {
        return supervisorGenerator;
    }
}

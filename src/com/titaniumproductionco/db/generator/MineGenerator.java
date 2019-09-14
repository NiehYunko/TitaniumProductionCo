package com.titaniumproductionco.db.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.titaniumproductionco.db.services.Role;

public class MineGenerator {

    private MiningLogGenerator lastMiningLogGen;
    private ArrayList<UserGenerator> supervisorGenerator = new ArrayList<>();

    public String[] generate(DataGenerator gen) {
        Random r = gen.rand();
        String city = Dictionary.randomCity(r);
        String state = r.nextFloat() < 0.2 ? null : Dictionary.randomState(r);
        String country = Dictionary.randomCountry(r);
        Calendar discoverDate = gen.randomDate(1);
        if (Calendar.getInstance().before(discoverDate)) {
            discoverDate.setTimeInMillis(System.currentTimeMillis());
        }
        Calendar estextDate = gen.randomDate(15);
        estextDate.setTimeInMillis(estextDate.getTimeInMillis() + 31556952000L);
        lastMiningLogGen = new MiningLogGenerator(nextID, discoverDate);
        supervisorGenerator.add(new UserGenerator(Role.MINE_SUPERVISOR, nextID));
        supervisorGenerator.add(new UserGenerator(Role.MINE_SUPERVISOR, nextID));
        return new String[] { String.valueOf(nextID++), city, state, country, DataGenerator.dateToString(discoverDate), DataGenerator.dateToString(estextDate) };
    }

    private int nextID = 1;

    public MiningLogGenerator getLastGeneratedMiningLogGenerator() {
        return lastMiningLogGen;
    }

    public List<UserGenerator> getSupervisorGenerators() {
        return supervisorGenerator;
    }
}

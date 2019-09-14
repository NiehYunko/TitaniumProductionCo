package com.titaniumproductionco.db.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.titaniumproductionco.db.parser.CSVWriter;
import com.titaniumproductionco.db.services.Role;

public class GeneratorTest {
    public static void main(String[] args) throws IOException {
        DataGenerator gen = new DataGenerator(2018);
        File f = new File("data.csv");
        writeTest(f, gen);
    }

    private static void writeTest(File f, DataGenerator gen) throws IOException {
        if (!f.exists())
            f.createNewFile();
        CSVWriter writer = new CSVWriter(f);
        writer.open();
        ArrayList<UserGenerator> userGen = new ArrayList<UserGenerator>();

        System.out.println("Generating Mine");
        ArrayList<MiningLogGenerator> minesGen = new ArrayList<>();
        MineGenerator m = new MineGenerator();
        writer.startSection("Mine");
        for (int i = 0; i < 3; i++) {
            writer.printEntry(m.generate(gen));
            minesGen.add(m.getLastGeneratedMiningLogGenerator());
        }
        userGen.addAll(m.getSupervisorGenerators());
        writer.endSection();
        System.out.println("Mine finished with " + minesGen.size() + " MiningLogGenerator");
        System.out.println("Generating MiningLog");

        ArrayList<ShipOresToGenerator> shipOresToGen = new ArrayList<>();
        writer.startSection("Mines");
        for (MiningLogGenerator g : minesGen) {
            String[][] logs = g.generateOnce(gen);
            while (logs != null) {
                for (String[] e : logs) {
                    writer.printEntry(e);
                }
                logs = g.generateOnce(gen);
            }
            shipOresToGen.addAll(g.getShipOresToGenerators());
        }
        writer.endSection();
        System.out.println("MininingLog finished with " + shipOresToGen.size() + " ShipOresToGenerator");
        System.out.println("Generating Factory");

        ArrayList<MachineGenerator> mGen = new ArrayList<>();
        FactoryGenerator fact = new FactoryGenerator();
        writer.startSection("Factory");
        for (int i = 0; i < 3; i++) {
            writer.printEntry(fact.generate(gen));
            mGen.add(fact.getLastGeneratedMachineGenerator());
        }
        userGen.addAll(fact.getSupervisorGenerators());
        writer.endSection();
        System.out.println("Factory finished with " + mGen.size() + " MachineGenerator");
        System.out.println("Generating ShipOresTo");

        writer.startSection("ShipOresTo");
        for (ShipOresToGenerator g : shipOresToGen) {
            writer.printEntry(g.generate(gen, fact));
        }
        writer.endSection();
        System.out.println("Generating Machine");

        ArrayList<ProcExecGenerator> peGen = new ArrayList<>();
        writer.startSection("Machine");
        int nextID = -1;
        for (MachineGenerator g : mGen) {
            if (nextID > 0)
                g.nextID = nextID;
            String[][] e = g.generate(gen);
            for (String[] str : e) {
                writer.printEntry(str);
            }
            nextID = g.nextID;
            peGen.addAll(g.getProcExecGenerators());
            userGen.addAll(g.getOpGenerators());
        }
        writer.endSection();
        System.out.println("Machine finished with " + peGen.size() + " ProcessExecGenerator");
        System.out.println("Generating ProcessExec");

        ArrayList<UsedInGenerator> usedGen = new ArrayList<>();
        writer.startSection("ProcessExecution");
        nextID = -1;
        for (ProcExecGenerator g : peGen) {
            if (nextID > 0)
                g.nextID = nextID;
            List<String[]> logs = g.generateOnce(gen);
            while (logs != null) {
                for (String[] str : logs) {
                    writer.printEntry(str);
                }
                logs = g.generateOnce(gen);
            }

            nextID = g.nextID;
            usedGen.addAll(g.getUsedInGenerators());
        }
        writer.endSection();
        System.out.println("ProcessExec finished with " + usedGen.size() + " UsedInGenerator");
        System.out.println("Generating UsedIn");

        writer.startSection("UsedIn");
        for (UsedInGenerator g : usedGen) {
            writer.printEntry(g.generate());
        }
        writer.endSection();
        System.out.println("Generating Order");
        writer.startSection("Order");
        OrderGenerator oGen = new OrderGenerator();
        String[][] orders = oGen.generateOnce(gen);
        while (orders != null) {
            for (String[] e : orders) {
                writer.printEntry(e);
            }
            orders = oGen.generateOnce(gen);
        }
        writer.endSection();
        System.out.println("Order finished with " + oGen.getShippedToGenerators().size() + " ShippedToGenerator");
        System.out.println("Generating ShippedTo");

        writer.startSection("ShippedTo");
        for (ShippedToGenerator g : oGen.getShippedToGenerators()) {
            String[] ship = g.generate(gen, fact);
            if (ship != null)
                writer.printEntry(ship);
        }
        writer.endSection();

        for (int i = 0; i < 5; i++) {
            userGen.add(new UserGenerator(Role.SALES_DEPART, 0));
        }
        for (int i = 0; i < 2; i++) {
            userGen.add(new UserGenerator(Role.HUMAN_RESOURCE, 0));
        }

        System.out.println("Generating " + userGen.size() + " users");
        writer.startSection("Login");
        for (int i = 0; i < userGen.size(); i++) {
            writer.printEntry(userGen.get(i).generate(i + 100, gen));
        }
        writer.endSection();

        System.out.println("Generating Roles");

        writer.startSection("MineSupervisor");
        for (UserGenerator user : userGen) {
            String[] e = user.generateArg(Role.MINE_SUPERVISOR);
            if (e != null)
                writer.printEntry(e);
        }
        writer.endSection();

        writer.startSection("FactorySupervisor");
        for (UserGenerator user : userGen) {
            String[] e = user.generateArg(Role.FACTORY_SUPERVISOR);
            if (e != null)
                writer.printEntry(e);
        }
        writer.endSection();

        writer.startSection("MachineOperator");
        for (UserGenerator user : userGen) {
            String[] e = user.generateArg(Role.MACHINE_OPERATOR);
            if (e != null)
                writer.printEntry(e);
        }
        writer.endSection();

        System.out.println("Finished!");
        writer.close();
    }

}

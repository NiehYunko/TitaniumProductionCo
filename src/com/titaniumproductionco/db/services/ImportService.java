package com.titaniumproductionco.db.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.titaniumproductionco.db.parser.CSVParser;
import com.titaniumproductionco.db.services.func.ErrorMes;
import com.titaniumproductionco.db.services.func.Proc;
import com.titaniumproductionco.db.services.func.ProcOutput;
import com.titaniumproductionco.db.services.func.TransactionTracker;
import com.titaniumproductionco.db.ui.component.DateSelector;

public class ImportService {
    public static File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("TPC Data", "csv"));
        chooser.setCurrentDirectory(new File("").getAbsoluteFile());
        int op = chooser.showOpenDialog(null);
        if (op == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        return null;
    }

    public static void importData(File csvFile) throws FileNotFoundException {
        CSVParser parser = new CSVParser(csvFile);
        parser.open();
        DBService.useTransaction("Data Imported Successfully!", ErrorMes.ERROR_MESSAGE, (tran) -> {
            ProcOutput.suppressed = true;
            parseCSVAndImport(parser, tran);
            ProcOutput.suppressed = false;
        });
    }

    private static void parseCSVAndImport(CSVParser parser, TransactionTracker tran) throws SQLException {
        String section = parser.nextSectionTitle();
        while (section != null) {
            System.out.println("[Import] Importing " + section);
            String[] e = parser.nextEntry();
            int count = 0;
            while (e != null) {
                count++;
                Object[] data = parse(section, e);
                if (data == null) {
                    System.out.println("[Import] Section Error: " + section);
                    tran.setRollback();
                    return;
                }
                Proc p = getImportProc(section);
                if (p == null) {
                    System.out.println("[Import] Section Error: " + section);
                    tran.setRollback();
                    return;
                }
                if (p.call(tran, null, data) == null) {
                    System.out.println("[Import] Import Error: " + Arrays.toString(data));
                    tran.setRollback();
                    return;
                }
                e = parser.nextEntry();
            }
            System.out.println("[Import] Section [" + section + "] finished with " + count + " entries.");
            section = parser.nextSectionTitle();
        }
        tran.setCommit();
        parser.close();
    }

    private static Object[] parse(String section, String[] data) {
        Object[] parsed = new Object[data.length];
        System.arraycopy(data, 0, parsed, 0, data.length);
        switch (section) {
        case "Mine":
            parsed[0] = integer(data[0]);
            parsed[4] = date(data[4]);
            parsed[5] = date(data[5]);
            break;
        case "Mines":
            parsed[0] = date(data[0]);
            parsed[1] = integer(data[1]);
            parsed[2] = integer(data[2]);
            parsed[3] = integer(data[3]);
            break;
        case "Factory":
            parsed[0] = integer(data[0]);
            break;
        case "ShipOresTo":
            parsed[0] = date(data[0]);
            parsed[1] = integer(data[1]);
            parsed[2] = integer(data[2]);
            parsed[3] = integer(data[3]);
            parsed[4] = integer(data[4]);
            break;
        case "Machine":
            parsed[0] = integer(data[0]);
            parsed[1] = decimal(data[1]);
            parsed[2] = date(data[2]);
            parsed[3] = integer(data[3]);
            parsed[4] = integer(data[4]);
            break;
        case "ProcessExecution":
            parsed[0] = integer(data[0]);
            parsed[1] = integer(data[1]);
            parsed[2] = DateSelector.parseTimeStamp(data[2]);
            break;
        case "UsedIn":
            parsed[0] = integer(data[0]);
            parsed[1] = integer(data[1]);
            parsed[2] = integer(data[2]);
            break;
        case "Order":
            parsed[0] = integer(data[0]);
            parsed[1] = date(data[1]);
            parsed[3] = integer(data[3]);
            break;
        case "ShippedTo":
            parsed[0] = integer(data[0]);
            parsed[1] = integer(data[1]);
            parsed[2] = date(data[2]);
            parsed[3] = date(data[3]);
            break;
        case "Login":
            parsed[0] = integer(data[0]);
            break;
        case "MineSupervisor":
        case "FactorySupervisor":
        case "MachineOperator":
            parsed[0] = integer(data[0]);
            parsed[1] = integer(data[1]);
            break;
        default:
            return null;
        }
        return parsed;
    }

    private static Integer integer(String i) {
        if (i == null)
            return null;
        return Integer.parseInt(i);
    }

    private static Double decimal(String d) {
        if (d == null)
            return null;
        return Double.parseDouble(d);
    }

    private static Date date(String d) {
        if (d == null)
            return null;
        return DateSelector.parseDate(d);
    }

    private static Proc getImportProc(String section) {
        switch (section) {
        case "Mine":
            return Proc.DB_Import_Mine;
        case "Mines":
            return Proc.DB_Import_Mines;
        case "Factory":
            return Proc.DB_Import_Factory;
        case "ShipOresTo":
            return Proc.DB_Import_ShipOresTo;
        case "Machine":
            return Proc.DB_Import_Machine;
        case "ProcessExecution":
            return Proc.DB_Import_ProcessExecution;
        case "UsedIn":
            return Proc.DB_Import_UsedIn;
        case "Order":
            return Proc.DB_Import_Order;
        case "ShippedTo":
            return Proc.DB_Import_ShippedTo;
        case "Login":
            return Proc.DB_Import_Login;
        case "MineSupervisor":
            return Proc.DB_Import_MineSupervisor;
        case "FactorySupervisor":
            return Proc.DB_Import_FactorySupervisor;
        case "MachineOperator":
            return Proc.DB_Import_MachineOperator;
        }
        return null;
    }
}

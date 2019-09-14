package com.titaniumproductionco.db.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVParser {
    private File file;
    private Scanner scan;
    public static final String NULL_INDICATER = "<NULL>";
    public static final String COMMA_INDICATOR = "<|>";
    public static final String SECTION_HEADER = "[[[";

    public CSVParser(File f) {
        file = f;
    }

    public void open() throws FileNotFoundException {
        scan = new Scanner(file, "UTF-8");
    }

    public void close() {
        scan.close();
    }

    public String nextSectionTitle() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.startsWith(SECTION_HEADER))
                return line.replace(SECTION_HEADER, "");
        }
        return null;
    }

    public String[] nextEntry() {
        if (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.length() == 0)
                return null;
            String[] split = line.split(",");
            for (int i = 0; i < split.length; i++) {
                String e = split[i].substring(1, split[i].length() - 1);
                if (e.equals(NULL_INDICATER))
                    e = null;
                else
                    e = e.replace(COMMA_INDICATOR, ",");
                split[i] = e;
            }
            return split;
        }
        return null;
    }

}

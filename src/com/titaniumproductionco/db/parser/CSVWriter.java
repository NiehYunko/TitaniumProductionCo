package com.titaniumproductionco.db.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CSVWriter {
    private File file;
    private PrintWriter print;
    private int lineCount;
    private static final int FLUSH = 100;

    public CSVWriter(File f) {
        file = f;
    }

    public void open() throws FileNotFoundException, UnsupportedEncodingException {
        print = new PrintWriter(file, "UTF-8");
    }

    public void close() {
        lineCount = 0;
        print.flush();
        print.close();
    }

    public void startSection(String section) {
        print.println();
        print.println(CSVParser.SECTION_HEADER + section);
        lineCount += 2;
        flush();
    }

    public void printEntry(String... entry) {
        print.println(toCSV(entry));
        lineCount++;
        flush();
    }

    private String toCSV(String... str) {
        StringBuilder build = new StringBuilder();
        build.append('"').append(checkCSVNull(str[0]));
        for (int i = 1; i < str.length; i++) {
            build.append("\",\"").append(checkCSVNull(str[i]));
        }
        build.append('"');
        return build.toString();
    }

    private String checkCSVNull(String str) {
        if (str == null)
            return CSVParser.NULL_INDICATER;
        return str.replace(",", CSVParser.COMMA_INDICATOR);
    }

    public void endSection() {
        print.println();
        lineCount++;
        flush();
    }

    private void flush() {
        if (lineCount > FLUSH) {
            lineCount = 0;
            print.flush();
        }
    }

}

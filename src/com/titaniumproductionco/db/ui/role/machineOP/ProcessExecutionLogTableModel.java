package com.titaniumproductionco.db.ui.role.machineOP;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ProcessExecutionLogTableModel implements TableModel {
    private Object[][] data;

    private class LogRow {
        int procID;
        String timestamp;
        String process;
        ArrayList<String> inputs = new ArrayList<>();
        ArrayList<String> outputs = new ArrayList<>();
    }

    public ProcessExecutionLogTableModel(ResultSet rs) throws SQLException {
        TreeMap<Integer, LogRow> map = new TreeMap<Integer, LogRow>();
        while (rs.next()) {
            int pID = rs.getInt("ProcExecID");
            LogRow row = map.get(pID);
            if (row == null) {
                row = new LogRow();
                map.put(pID, row);
                String timestamp = rs.getTimestamp("Timestamp").toString();
                String process = rs.getString("ProcessName");
                row.procID = pID;
                row.timestamp = timestamp;
                row.process = process;
            }
            String material = rs.getInt("Moles") + " x [" + rs.getString("Material") + "]";
            if (rs.getBoolean("Is Input")) {
                row.inputs.add(material);
            } else {
                row.outputs.add(material);
            }
        }

        data = new Object[map.size()][4];
        int i = 0;
        for (int row : map.navigableKeySet()) {
            LogRow log = map.get(row);
            data[i][0] = String.valueOf(log.procID);
            data[i][1] = log.timestamp;
            data[i][2] = log.process;
            StringBuilder build = new StringBuilder("<html><b>Inputs</b>");
            for (String mat : log.inputs) {
                build.append("<br>").append(mat);
            }
            build.append("<br><b>Outputs</b>");
            for (String mat : log.outputs) {
                build.append("<br>").append(mat);
            }
            build.append("</html>");
            data[i][3] = build.toString();
            i++;
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return String.class;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
        case 0:
            return "ExecutionID";
        case 1:
            return "Timestamp";
        case 2:
            return "Process";
        case 3:
            return "Material Usage";
        }
        return "";
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public Object getValueAt(int r, int c) {
        return data[r][c];
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    @Override
    public void addTableModelListener(TableModelListener arg0) {

    }

    @Override
    public void removeTableModelListener(TableModelListener arg0) {

    }

    @Override
    public void setValueAt(Object arg0, int arg1, int arg2) {
        // cannot set
    }

}

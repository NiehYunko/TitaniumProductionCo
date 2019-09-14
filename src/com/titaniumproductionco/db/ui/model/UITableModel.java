package com.titaniumproductionco.db.ui.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class UITableModel implements TableModel {
    private static RowSetFactory rowSetFactory = null;
    private CachedRowSet rowset;
    private ResultSetMetaData metadata;
    public int numcols;
    public int numrows;

    private UITableModel(CachedRowSet rowSetArg) throws SQLException {

        this.rowset = rowSetArg;
        this.metadata = this.rowset.getMetaData();
        this.numcols = metadata.getColumnCount();

        // Retrieve the number of rows.
        this.rowset.beforeFirst();
        this.numrows = 0;
        while (this.rowset.next()) {
            this.numrows++;
        }
        this.rowset.beforeFirst();
    }

    @Override
    public void addTableModelListener(TableModelListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    @Override
    public int getColumnCount() {
        return this.numcols;
    }

    @Override
    public String getColumnName(int arg0) {
        try {
            return this.metadata.getColumnLabel(arg0 + 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int getRowCount() {
        return this.numrows;
    }

    @Override
    public Object getValueAt(int arg0, int arg1) {
        try {
            this.rowset.absolute(arg0 + 1);
            Object o = this.rowset.getObject(arg1 + 1);
            if (o == null)
                return null;
            else
                return o.toString();
        } catch (SQLException e) {
            return e.toString();
        }
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    @Override
    public void removeTableModelListener(TableModelListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValueAt(Object arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException();
    }

    /**
     * Convert a resultset to a table model
     * 
     * @param result
     * @return
     * @throws SQLException
     */
    public static UITableModel createTableModel(ResultSet result) throws SQLException {
        if (rowSetFactory == null) {
            rowSetFactory = RowSetProvider.newFactory();
        }
        CachedRowSet c = rowSetFactory.createCachedRowSet();
        c.populate(result);
        return new UITableModel(c);
    }

}

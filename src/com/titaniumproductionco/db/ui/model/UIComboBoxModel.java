package com.titaniumproductionco.db.ui.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class UIComboBoxModel implements ComboBoxModel<String> {
    private String[] data;
    private String selected;

    public static UIComboBoxModel createComboBoxModel(ResultSet result) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        while (result.next()) {
            list.add(result.getString(1));
        }
        String[] arr = list.toArray(new String[list.size()]);
        return new UIComboBoxModel(arr);
    }

    private UIComboBoxModel(String[] dat) {
        data = dat;
        selected = null;
    }

    @Override
    public void addListDataListener(ListDataListener arg0) {
    }

    @Override
    public String getElementAt(int i) {
        return data[i];
    }

    @Override
    public int getSize() {
        return data.length;
    }

    @Override
    public void removeListDataListener(ListDataListener arg0) {
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public void setSelectedItem(Object sel) {
        selected = (String) sel;
    }

}

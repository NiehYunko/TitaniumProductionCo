package com.titaniumproductionco.db.ui.role.sales;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.Proc;
import com.titaniumproductionco.db.ui.component.DateSelector;
import com.titaniumproductionco.db.ui.component.IntegerField;

@SuppressWarnings("serial")
public class SalesOrderAddModPanel extends JPanel {
    private DateSelector dateSelector;
    private IntegerField quantityField;
    private JTextField addressField;
    private int updateOrderID;

    public SalesOrderAddModPanel(boolean add) {
        this.setPreferredSize(new Dimension(350, 270));
        this.setLayout(new GridLayout(7, 1));
        this.add(new JLabel("Start Placed Date"));
        this.add(dateSelector = new DateSelector());
        this.add(new JLabel("Shipp Address"));
        this.add(addressField = new JTextField());
        this.add(new JLabel("Quantity"));
        this.add(quantityField = new IntegerField());

        if (add) {
            JButton button = new JButton();
            button.setText("Add");
            button.addActionListener(this::handleAdd);
            this.add(button);
        }

    }

    public void setOld(int orderID, Date oDate, int oQuantity, String address) {
        updateOrderID = orderID;
        dateSelector.setDate(oDate);
        quantityField.setText(String.valueOf(oQuantity));
        addressField.setText(address);
    }

    private boolean validateInputs() {
        if (!quantityField.isInputValidPos() || addressField.getText().length() <= 0) {
            JOptionPane.showMessageDialog(this, "<html>Please make sure quantity is valid.<br>Please make sure address is not empty.</html>", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!DBService.validateRoleWithError(this, Role.SALES_DEPART))
            return false;
        return true;
    }

    private boolean handleAdd(ActionEvent e) {
        if (!validateInputs())
            return false;
        Date date = dateSelector.getDate();
        String address = addressField.getText();
        int quantity = quantityField.getParsedInt();

        return Proc.Sales_AddOrder.call(this, date, address, quantity) != null;
    }

    public boolean handleUpdate() {
        if (!validateInputs())
            return false;
        Date date = dateSelector.getDate();
        String address = addressField.getText();
        int quantity = quantityField.getParsedInt();
        return Proc.Sales_UpdateOrder.call(this, updateOrderID, date, address, quantity) != null;
    }

}
